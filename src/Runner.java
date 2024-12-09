public class Runner {
    public static void main(String[] args) {
        ParserClass parser = new ParserClass();

        String inputPath = "C:/Users/Mobil/Desktop/repo/IdeaProjects/PNRPU_AI_Lab_XML_Parser/input/";
        String outputPath = "C:/Users/Mobil/Desktop/repo/IdeaProjects/PNRPU_AI_Lab_XML_Parser/output/";

        parser.setOutputPath(outputPath);
        for (int i = 1; i <= 10; i++){
            String inputFilePath = inputPath;
            inputFilePath += String.format("ТП%d.xml", i);
            System.out.println(inputFilePath);
            parser.setInputPath(inputFilePath);
            parser.parseFile(inputFilePath);
        }
    }
}
