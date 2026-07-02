package airhacks.zsmith.agent.control;

import java.nio.file.Files;
import java.nio.file.Path;

public interface Version {

    String versionFile = "version.txt";
    Path versionPath = Path.of(versionFile);

    public static String current() {
        var fromManifest = Version.class.getPackage().getImplementationVersion();
        if (fromManifest != null) {
            return fromManifest.strip();
        }
        try (var in = Version.class.getClassLoader().getResourceAsStream(versionFile)) {
            if (in != null) {
                return new String(in.readAllBytes()).strip();
            }
        } catch (Exception e) {
            throw new IllegalStateException("Cannot read " + versionPath + " from classpath", e);
        }
        return fromFilesystem();
    }

    static String fromFilesystem() {
        try {
            return Files.readString(versionPath).strip();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot read " + versionPath, e);
        }
    }
}
