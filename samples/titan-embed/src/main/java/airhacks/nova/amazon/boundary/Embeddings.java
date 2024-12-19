package airhacks.nova.amazon.boundary;

import airhacks.nova.amazon.control.Titan;
import airhacks.nova.bedrock.control.Bedrock;

public interface Embeddings {

    boolean debug = true;

    static void info(){
        Bedrock.listModels();
    }
    
    static double[] createEmbeddings(String text,int dimensions){
        var response =  Bedrock.createEmbeddings(text, dimensions);
        return Titan.extractEmbeddings(response);
    }

}
