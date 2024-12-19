package airhacks.sentimental.control;

import java.io.IOException;

import com.github.tjake.jlama.model.AbstractModel;
import com.github.tjake.jlama.model.ModelSupport;
import com.github.tjake.jlama.safetensors.DType;
import com.github.tjake.jlama.safetensors.SafeTensorSupport;

public interface ModelLoader {
    enum Model{
        TINY("tjake/TinyLlama-1.1B-Chat-v1.0-Jlama-Q4"),
        MISTRAL7B("tjake/Mistral-7B-Instruct-v0.3-JQ4"),
        MIXTRAL("tjake/Mixtral-8x7B-Instruct-v0.1-JQ4"),
        LLAMA("tjake/Llama-3.2-1B-Instruct-Jlama-Q4");
        
        final String modelName;
        
        private Model(String modelName) {
            this.modelName = modelName;
        }

        public String modelName(){
            return this.modelName;
        }


    }
        String workingDirectory = "./models";


    static AbstractModel load() throws IOException{
        var localModelPath = SafeTensorSupport
        .maybeDownloadModel(workingDirectory, Model.LLAMA.modelName());
        return ModelSupport.loadModel(localModelPath, DType.F32, DType.I8);
    }

}
