package airhacks;

import airhacks.sentimental.boundary.SentimentAnalysis;

/**
 *
 * @author airhacks.com
 */
interface App {

    static void main(String... args) {
        var message = args.length > 0 ? args[0]: "java is great";
        var result = SentimentAnalysis.analyze(message);
        System.out.println(result);
    }
}
