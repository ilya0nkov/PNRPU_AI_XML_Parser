import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.FilenameFilter;
import java.io.*;

public class Main {

    private static String outputPath = "";
    private static String inputPath = "";
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        boolean isDir = false;

        try{
            System.out.println("Как будут заданы входные данные?:\n" +
                    "Введите цифру\n" +
                    "f - если будет задан путь до файла\n" +
                    "d - если будет задан путь до директории ");
            input = scanner.next();
            if (input.equals("d")){
                isDir = true;
            }
            else if (!input.equals("f")){
                System.out.println("Некорректный ввод");
                System.exit(0);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Введите путь к файлу/директории\n" +
                "с входными данными в одном из форматов:\n" +
                "C:/Users/User/dir/file.xml\n" +
                "C:\\Users\\User\\dir\\file.xml\n" +
                "C:/Users/User/dir/\n" +
                "C:\\Users\\User\\dir\\");
        scanner.nextLine();
        String inPath = scanner.nextLine();
        if (isDir){
            checkDirAccess(inPath);
        }
        inputPath = inPath;

        System.out.println("Введите путь к директории\n" +
                "для выходных данных:\n" +
                "C:/Users/User/dir/file.xml\n" +
                "C:\\Users\\User\\dir\\file.xml\n" +
                "C:/Users/User/dir/\n" +
                "C:\\Users\\User\\dir\\");
        String outPath = scanner.nextLine();
        if (isDir){
            checkDirAccess(outPath);
        }
        outputPath = outPath;
        if (!isDir){
            parseFile(inPath);
            System.out.printf("Обработан файл: " + inPath);
        }
        else {
            parseDir(inPath);
        }
    }

    public static void parseFile(String path) {
        try {
            String xmlContent = readFileAsString(path);

            // Удаление лишних символов перед декларацией XML
            xmlContent = xmlContent.trim();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            File xmlFile = new File(path);
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            Element parent = (Element) document.getElementsByTagName("PARENT").item(0);

            // <PARENT>
            //System.out.println("----- PARENT -----");
            extractAndPrint(parent, "PRAVILO_REV");
            extractAndPrint(parent, "IZDELIE");
            extractAndPrint(parent, "PARENT_OBOZN");
            extractAndPrint(parent, "PARENT_NAME");
            extractAndPrint(parent, "PARENT_TYPE");
            extractAndPrint(parent, "PARENT_REV");
            extractAndPrint(parent, "PARENT_STATUS");
            extractAndPrint(parent, "PARENT_TIME");
            extractAndPrint(parent, "PARENT_N_RASP");
            extractAndPrint(parent, "PARENT_N_ZAKAZ");
            extractAndPrint(parent, "PARENT_SZ");
            extractAndPrint(parent, "PARENT_DESC");
            extractAndPrint(parent, "PARENT_VID");
            extractAndPrint(parent, "PARENT_TYPE_TD");
            extractAndPrint(parent, "PARENT_TRUD");
            extractAndPrint(parent, "PARENT_S_TTP");
            extractAndPrint(parent, "PARENT_ETAP");
            extractAndPrint(parent, "PARENT_ETAPOV");
            extractAndPrint(parent, "PARENT_CREATOR");
            extractAndPrint(parent, "PARENT_DATE_CREATED");
            extractAndPrint(parent, "PARENT_DEPART");
            extractAndPrint(parent, "PARENT_IDMAINDOC");
            extractAndPrint(parent, "PARENT_DATE_IZM");
            extractAndPrint(parent, "PARENT_N_IZV");

            // <TARGETS>
            Element targets = (Element) parent.getElementsByTagName("TARGETS").item(0);
            //System.out.println("\n----- TARGETS -----");
            extractAndPrint(targets, "TARGETS_COUNT");

            NodeList targetObjects = targets.getElementsByTagName("TRG_OBJ");
            for (int i = 0; i < targetObjects.getLength(); i++) {
                Element targetObject = (Element) targetObjects.item(i);
                //System.out.println("\n  -- TRG_OBJ --");
                extractAndPrint(targetObject, "OBJ_OBOZN");
                extractAndPrint(targetObject, "OBJ_NAME");
                extractAndPrint(targetObject, "OBJ_TYPE");
                extractAndPrint(targetObject, "OBJ_MD");
            }

            // <WORKAREA>
            Element workarea = (Element) parent.getElementsByTagName("WORKAREA").item(0);
            //System.out.println("\n----- WORKAREA -----");
            extractAndPrint(workarea, "WORKAREA_COUNT");

            NodeList workAreaObjects = workarea.getElementsByTagName("WA_OBJ");
            for (int i = 0; i < workAreaObjects.getLength(); i++) {
                Element workAreaObject = (Element) workAreaObjects.item(i);
                //System.out.println("\n  -- WA_OBJ --");
                extractAndPrint(workAreaObject, "OBJ_OBOZN");
                extractAndPrint(workAreaObject, "OBJ_NAME");
                // значение OBJ_TYPE всегда одинаково
                // extractAndPrint(workAreaObject, "OBJ_TYPE");
            }

            // <FILES>
            Element files = (Element) parent.getElementsByTagName("FILES").item(0);
            //System.out.println("\n----- FILES -----");
            extractAndPrint(files, "FILES_COUNT");

            NodeList fileObjects = files.getElementsByTagName("FILE");
            for (int i = 0; i < fileObjects.getLength(); i++) {
                Element fileObject = (Element) fileObjects.item(i);
                //System.out.println("\n  -- FILE --");
                extractAndPrint(fileObject, "PATH");
                extractAndPrint(fileObject, "NAME");
            }

            // <CHILDS_OBJ>
            Element childsObj = (Element) parent.getElementsByTagName("CHILDS_OBJ").item(0);
            String opersCountString = getValue(childsObj, "OPERS_COUNT");
            assert opersCountString != null;
            int opersCount = Integer.parseInt(opersCountString);

            //System.out.println("\n----- CHILDS_OBJ -----");
            extractAndPrint(childsObj, "OPERS_COUNT");
            extractAndPrint(childsObj, "ZAGOT_COUNT");

            NodeList zagObjects = childsObj.getElementsByTagName("ZAG_OBJ");
            for (int i = 0; i < zagObjects.getLength(); i++) {
                Element zagObject = (Element) zagObjects.item(i);
                //System.out.println("\n  -- ZAG_OBJ --");
                extractAndPrint(zagObject, "OBJ_OBOZN");
                extractAndPrint(zagObject, "OBJ_NAME");
                extractAndPrint(zagObject, "OBJ_POZ");
                extractAndPrint(zagObject, "OBJ_DESC");
                extractAndPrint(zagObject, "MATERIAL");
                extractAndPrint(zagObject, "MARKA_KE");
                extractAndPrint(zagObject, "EV");
                extractAndPrint(zagObject, "NORMA");
                extractAndPrint(zagObject, "OBJ_KIM");
                extractAndPrint(zagObject, "VID_Z");
                extractAndPrint(zagObject, "PR_I_RAZ");
                extractAndPrint(zagObject, "KOL");
                extractAndPrint(zagObject, "M_ZAG");
                extractAndPrint(zagObject, "OTHOD");
                extractAndPrint(zagObject, "GR_OTH");
                extractAndPrint(zagObject, "PROFIL");

                // TODO: узнать бывают ли значения кроме 0
                /*
                extractAndPrint(childObject, "D");
                extractAndPrint(childObject, "L");
                extractAndPrint(childObject, "S");
                extractAndPrint(childObject, "B");
                extractAndPrint(childObject, "RS");
                extractAndPrint(childObject, "SK");
                extractAndPrint(childObject, "DV");
                extractAndPrint(childObject, "N_SETKI");
                 */
            }
            // <CHILDS_OBJ><OBJ></CHILDS_OBJ>
            //System.out.println("\n----- OBj (CHILDS_OBJ) -----");
            NodeList childObjects = childsObj.getElementsByTagName("OBJ");
            for (int i = 0; i < opersCount; i++) {
                Element childObject = (Element) childObjects.item(i);
                //System.out.println("\n  -- OBJ --");
                extractAndPrint(childObject, "OBJ_NAME");
                extractAndPrint(childObject, "OBJ_TYPE");
                extractAndPrint(childObject, "OBJ_POZ");
                extractAndPrint(childObject, "OBJ_TIME");
                extractAndPrint(childObject, "OBJ_DESC");
                extractAndPrint(childObject, "OBJ_DOP_INF");
                extractAndPrint(childObject, "OBJ_RESOURCE");
                extractAndPrint(childObject, "OBJ_REJ_VYP");

                Element resurses = (Element) childObject.getElementsByTagName("RESURS").item(0);
                String resursCountString = getValue(resurses, "RESURS_COUNT");
                if (resursCountString == null){
                    continue;
                }
                int resursCount = Integer.parseInt(resursCountString);

                //System.out.println("\n--- RESURS ---");
                extractAndPrint(resurses, "RESURS_COUNT");

                for (int j = 0; j < resursCount; j++){
                    Element resurs = (Element) resurses.getElementsByTagName("RES_OBJ").item(j);
                    //System.out.println("-- RES_OBJ --");
                    extractAndPrint(resurs, "OBJ_OBOZN");
                    extractAndPrint(resurs, "OBJ_NAME");
                    extractAndPrint(resurs, "OBJ_TYPE");
                    extractAndPrint(resurs, "OCC_TYPE");
                    extractAndPrint(resurs, "OBJ_POZ");
                    extractAndPrint(resurs, "OBJ_PACK_COUNT");
                    extractAndPrint(resurs, "OBJ_COUNT");
                    extractAndPrint(resurs, "OBJ_UNIT");
                    extractAndPrint(resurs, "OBJ_IS_MACH");
                    extractAndPrint(resurs, "OBJ_ABS_XFORM");
                    extractAndPrint(resurs, "OBJ_OCC_XFORM");
                    extractAndPrint(resurs, "OBJ_DOC_POSTAV");
                }

                Element params = (Element) childObject.getElementsByTagName("PARAMS").item(0);
                String paramsCountString = getValue(params, "PARAMS_COUNT");
                if (paramsCountString == null){
                    continue;
                }
                int paramsCount = Integer.parseInt(paramsCountString);

                //System.out.println("\n--- PARAMS ---");
                extractAndPrint(resurses, "PARAMS_COUNT");

                for (int j = 0; j < paramsCount; j++){
                    Element param = (Element) params.getElementsByTagName("PARAM").item(j);
                    //System.out.println("-- PARAM --");
                    extractAndPrint(param, "PARAM_TYPE");
                    extractAndPrint(param, "PARAM_NUM");
                    extractAndPrint(param, "PARAM_DESC");
                    extractAndPrint(param, "PARAM_PRIM");
                    extractAndPrint(param, "PARAM_UNIT");
                    extractAndPrint(param, "PARAM_N_VAL");
                    extractAndPrint(param, "PARAM_F_VAL");
                    extractAndPrint(param, "PARAM_F_DATE");
                    extractAndPrint(param, "PARAM_F_PERSON");
                    extractAndPrint(param, "PARAM_D_MAX");
                    extractAndPrint(param, "PARAM_D_MIN");
                    extractAndPrint(param, "PARAM_NAME");
                    extractAndPrint(param, "PARAM_KVALIT");
                    extractAndPrint(param, "PARAM_V_MAX");
                    extractAndPrint(param, "PARAM_V_MIN");
                    extractAndPrint(param, "PARAM_SIGN");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void extractAndPrint(Element element, String tagName) {
        File outputFile = new File(outputPath);

        if (outputFile.isDirectory()) {
            String inputFileName = new File(inputPath).getName();
            String baseName = inputFileName.substring(0, inputFileName.lastIndexOf('.'));
            String newFileName = baseName + "_parsed.txt";
            outputFile = new File(outputFile, newFileName);
        }

        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            String content = nodeList.item(0).getTextContent();

            // Проверяем, что content не пустой и не null
            if (content != null && !content.trim().isEmpty() && !content.equals(".")) {
                try (FileWriter writer = new FileWriter(outputFile, true)) {
                    writer.write(tagName + ": " + content + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (content == null && tagName.equals("EV")) {
                // Специальная проверка для тега EV, если значение равно null, записываем "шт"
                try (FileWriter writer = new FileWriter(outputFile, true)) {
                    writer.write(tagName + ": шт\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static String getValue(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return null;
    }

    private static void parseDir(String inputDir) {
        File directory = new File(inputDir);

        // Фильтр для получения только XML файлов
        if (!directory.isDirectory()) {
            System.out.println("Указанный путь не является директорией: " + inputDir);
            return;
        }
        File[] xmlFiles = directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".xml");
            }
        });

        // Проверка наличия XML файлов в директории
        if (xmlFiles != null && xmlFiles.length > 0) {
            for (File xmlFile : xmlFiles) {
                String fullPath = xmlFile.getAbsolutePath();  // Получаем полный путь к файлу
                System.out.println("Текущий файл: " + fullPath);
                parseFile(fullPath);  // Передаем полный путь в метод parseFile
            }
        } else {
            System.out.println("Нет XML файлов в директории");
        }
    }

    private static String readFileAsString(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    private static void checkDirAccess(String inputDir){
        File directory = new File(inputDir);

        if (!directory.exists()) {
            System.out.println("Директория не существует: " + inputDir);
            System.exit(0);
        } else if (!directory.isDirectory()) {
            System.out.println("Путь не является директорией: " + inputDir);
            System.exit(0);
        } else if (!directory.canRead()) {
            System.out.println("Нет прав на чтение из директории: " + inputDir);
            System.exit(0);
        } else if (!directory.canWrite()) {
            System.out.println("Нет прав на запись в директорию: " + inputDir);
            System.exit(0);
        } else {
            System.out.println("Директория доступна для чтения и записи: " + inputDir);
        }
    }
}
