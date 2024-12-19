package airhacks.nova.amazon.boundary;

import airhacks.nova.amazon.control.Nova;
import airhacks.nova.bedrock.control.Bedrock;

public interface GenAI {

    boolean debug = false;

    static void info(){
        Bedrock.listModels();
    }
    
    static String infer(String prompt){
        var response =  Bedrock.invokeNova(prompt, 100,0.0f);
        return Nova.extractNovaText(response);
    }
}
