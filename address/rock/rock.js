// hard rock stage visual — single fullscreen fragment shader, WebGL1, zero dependencies.
// palette from ../DESIGN.md: black canvas, MTV yellow #f7f908, signal teal #00aacd.

const VERTEX = `
attribute vec2 a_pos;
void main() {
  gl_Position = vec4(a_pos, 0.0, 1.0);
}
`;

const FRAGMENT = `
precision highp float;

uniform float u_time;
uniform vec2  u_resolution;
uniform float u_motion; // 1.0 full show, ~0.1 with prefers-reduced-motion

const float BPM = 140.0;

float hash(float n) {
  return fract(sin(n) * 43758.5453123);
}
float hash2(vec2 p) {
  return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453123);
}
float vnoise(vec2 p) {
  vec2 i = floor(p);
  vec2 f = fract(p);
  f = f * f * (3.0 - 2.0 * f);
  return mix(
    mix(hash2(i),                 hash2(i + vec2(1.0, 0.0)), f.x),
    mix(hash2(i + vec2(0.0, 1.0)), hash2(i + vec2(1.0, 1.0)), f.x),
    f.y);
}
float fbm(vec2 p) {
  float s = 0.0;
  float a = 0.5;
  for (int i = 0; i < 4; i++) {
    s += a * vnoise(p);
    p = p * 2.03 + vec2(1.7, 9.2);
    a *= 0.5;
  }
  return s;
}

// jagged oscilloscope path: MTV zigzag + per-segment glitch jitter
float wave(float x, float t, float pulse) {
  float tri = abs(fract(x * 3.0 - t * 1.2) * 2.0 - 1.0) * 2.0 - 1.0;
  float seg = floor(x * 24.0);
  float jag = hash(seg * 1.3 + floor(t * 24.0) * 57.1) - 0.5;
  float amp = 0.05 + 0.16 * pulse;
  return tri * amp * 0.55 + jag * amp * (0.3 + 0.7 * u_motion);
}

void main() {
  vec2 uv = (gl_FragCoord.xy - 0.5 * u_resolution) / u_resolution.y;
  float t = u_time;

  // --- the beat -------------------------------------------------------
  float beat  = t * BPM / 60.0;
  float pulse = pow(1.0 - fract(beat), 6.0); // kick decay every beat

  // screen shake on the kick
  float shake = pulse * 0.012 * u_motion;
  uv += (vec2(hash(floor(t * 47.0)), hash(floor(t * 47.0) + 7.0)) - 0.5) * shake;

  vec3 col = vec3(0.0);
  vec3 yellow = vec3(0.969, 0.976, 0.031); // #f7f908
  vec3 teal   = vec3(0.0,   0.667, 0.804); // #00aacd

  // --- smoke, breathing with the beat ---------------------------------
  float smoke = fbm(uv * 2.5 + vec2(t * 0.06, -t * 0.02));
  col += vec3(0.16, 0.11, 0.05) * smoke * (0.6 + 1.1 * pulse);
  // hot stage light from below
  col += yellow * smoothstep(0.1, -0.55, uv.y) * smoke * (0.08 + 0.25 * pulse);

  // --- teal echo wave, half tempo, behind everything ------------------
  float d2 = abs(uv.y + wave(uv.x + 0.7, t * 0.55, pulse * 0.5) * 0.7);
  col += teal * (0.0014 / (d2 + 0.003));

  // --- main waveform: yellow glow, white-hot core, chroma fringes -----
  float dC = abs(uv.y - wave(uv.x, t, pulse));
  float dR = abs(uv.y - wave(uv.x - 0.012 * pulse, t, pulse));
  float dB = abs(uv.y - wave(uv.x + 0.012 * pulse, t, pulse));
  col += yellow * (0.007 / (dC + 0.0025));
  col += vec3(1.0, 1.0, 0.75) * smoothstep(0.004, 0.0, dC) * 0.7;
  col.r += 0.5  * (0.002 / (dR + 0.002));
  col.b += 0.35 * (0.002 / (dB + 0.002));

  // --- lightning strikes ----------------------------------------------
  float cell  = floor(t / 1.7);
  float ft    = fract(t / 1.7);
  float flash = exp(-ft * 10.0) * step(0.4, hash(cell)) * u_motion;
  float ky = floor((uv.y + 0.5) * 10.0);
  float fy = fract((uv.y + 0.5) * 10.0);
  float bx = mix(hash(ky + cell * 91.0), hash(ky + 1.0 + cell * 91.0), fy) - 0.5;
  bx = bx * 0.5 + (hash(cell + 3.0) * 1.4 - 0.7);
  float bd = abs(uv.x - bx);
  col += vec3(0.85, 0.95, 1.0) * flash * (0.007 / (bd + 0.003));
  col += vec3(0.30, 0.50, 0.60) * flash * 0.35; // ambient flash

  // --- strobe on random accent beats ----------------------------------
  col += vec3(pulse * step(0.8, hash(floor(beat) + 31.0)) * 0.45 * u_motion);

  // --- finish: vignette, scanlines, grain -----------------------------
  col *= clamp(1.0 - dot(uv, uv) * 1.4, 0.0, 1.0);
  col *= 0.92 + 0.08 * sin(gl_FragCoord.y * 2.6);
  col += (hash2(gl_FragCoord.xy + fract(t) * 61.7) - 0.5) * 0.07;

  gl_FragColor = vec4(pow(max(col, 0.0), vec3(0.9)), 1.0);
}
`;

const canvas = document.querySelector('canvas');
const gl = canvas.getContext('webgl', { antialias: false });
if (!gl) {
  document.querySelector('.hint').textContent = 'WebGL is not available in this browser.';
  throw new Error('WebGL unavailable');
}

const compile = (type, source) => {
  const shader = gl.createShader(type);
  gl.shaderSource(shader, source);
  gl.compileShader(shader);
  if (!gl.getShaderParameter(shader, gl.COMPILE_STATUS)) {
    throw new Error(gl.getShaderInfoLog(shader));
  }
  return shader;
};

const program = gl.createProgram();
gl.attachShader(program, compile(gl.VERTEX_SHADER, VERTEX));
gl.attachShader(program, compile(gl.FRAGMENT_SHADER, FRAGMENT));
gl.linkProgram(program);
if (!gl.getProgramParameter(program, gl.LINK_STATUS)) {
  throw new Error(gl.getProgramInfoLog(program));
}
gl.useProgram(program);

// one oversized triangle covers the viewport
const buffer = gl.createBuffer();
gl.bindBuffer(gl.ARRAY_BUFFER, buffer);
gl.bufferData(gl.ARRAY_BUFFER, new Float32Array([-1, -1, 3, -1, -1, 3]), gl.STATIC_DRAW);
const aPos = gl.getAttribLocation(program, 'a_pos');
gl.enableVertexAttribArray(aPos);
gl.vertexAttribPointer(aPos, 2, gl.FLOAT, false, 0, 0);

const uTime = gl.getUniformLocation(program, 'u_time');
const uResolution = gl.getUniformLocation(program, 'u_resolution');
const uMotion = gl.getUniformLocation(program, 'u_motion');

const reducedMotion = matchMedia('(prefers-reduced-motion: reduce)');
const motion = () => (reducedMotion.matches ? 0.1 : 1.0);

const resize = () => {
  const dpr = Math.min(devicePixelRatio || 1, 2);
  canvas.width = Math.round(canvas.clientWidth * dpr);
  canvas.height = Math.round(canvas.clientHeight * dpr);
  gl.viewport(0, 0, canvas.width, canvas.height);
};
addEventListener('resize', resize);
resize();

let playing = true;
let elapsed = 0;
let previous = performance.now();

const frame = (now) => {
  if (playing) {
    elapsed += (now - previous) / 1000;
  }
  previous = now;
  gl.uniform1f(uTime, elapsed);
  gl.uniform2f(uResolution, canvas.width, canvas.height);
  gl.uniform1f(uMotion, motion());
  gl.drawArrays(gl.TRIANGLES, 0, 3);
  requestAnimationFrame(frame);
};
requestAnimationFrame(frame);

const toggle = () => { playing = !playing; };
canvas.addEventListener('click', toggle);
addEventListener('keydown', (event) => {
  if (event.code === 'Space') {
    event.preventDefault();
    toggle();
  }
});
