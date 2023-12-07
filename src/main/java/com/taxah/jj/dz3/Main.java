package com.taxah.jj.dz3;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.*;

/*
1. Разработайте класс Student с полями String name, int age, transient double GPA (средний балл).
Обеспечьте поддержку сериализации для этого класса.
Создайте объект класса Student и инициализируйте его данными.
Сериализуйте этот объект в файл.
Десериализуйте объект обратно в программу из файла.
Выведите все поля объекта, включая GPA, и обсудите, почему значение GPA не было сохранено/восстановлено.

2. * Выполнить задачу 1 используя другие типы сериализаторов (в xml и json документы).
 */

/**
 * The {@code Main} class serves as the entry point for the application, demonstrating
 * the serialization and deserialization of a {@code Student} object using JSON, binary,
 * and XML formats.
 *
 * <p>The class contains methods to save a {@code Student} object to a file in different
 * formats and to load a {@code Student} object from a file. It utilizes the Jackson
 * ObjectMapper for JSON and XML serialization, and standard Java Object Input/Output
 * Streams for binary serialization.
 *
 * <p>The default file paths and names for JSON, binary, and XML files are specified
 * as constants. The class showcases the serialization and deserialization process
 * for the GPA field using custom serialization/deserialization classes defined in
 * the {@code Student} class.
 *
 * <p>Dependencies: Jackson Databind library for JSON and XML processing.
 *
 * @author Anton Takhayev
 * @version 1.0
 * @since 07-12-2023
 */
public class Main {
    private static final String PATH = "src\\main\\resources\\";
    public static final String FILE_JSON = PATH + "task.json";
    public static final String FILE_BIN = PATH + "task.bin";
    public static final String FILE_XML = PATH + "task.xml";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final XmlMapper xmlMapper = new XmlMapper();

    /**
     * The main method of the application. Demonstrates the serialization and
     * deserialization of a {@code Student} object in JSON, binary, and XML formats.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {

        Student student = new Student("Anton", 36, 5.0);
        saveStudentToFile(FILE_JSON, student);
        saveStudentToFile(FILE_BIN, student);
        saveStudentToFile(FILE_XML, student);

        Student studentJSON = loadStudentFromFile(FILE_JSON);
        System.out.println("JSON serializer: " + studentJSON);
        System.out.println("The GPA field showed \"0.0\" because I used a custom class for" +
                "serialization/deserialization where I specified the required value of the hidden field\n");

        Student studentBIN = loadStudentFromFile(FILE_BIN);
        System.out.println("BIN serializer: " + studentBIN);
        System.out.println("The GPA field showed \"0.0\" because I indicated which fields are needed to " +
                "serialize/deserialize in methods writeExternal(), readExternal()\n");

        Student studentXML = loadStudentFromFile(FILE_XML);
        System.out.println("XML serializer: " + studentXML);
        System.out.println("The GPA field showed \"0.0\" because I used a custom class for " +
                "serialization/deserialization where I specified the required value of the hidden field\n");

    }

    /**
     * Saves a {@code Student} object to a file in JSON, binary, or XML format based on the
     * file extension.
     *
     * @param fileName The name of the file to save the {@code Student} object to.
     * @param student  The {@code Student} object to be saved.
     */
    public static void saveStudentToFile(String fileName, Student student) {
        try {
            if (fileName.endsWith(".json")) {
                objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
                objectMapper.writeValue(new File(fileName), student);
            } else if (fileName.endsWith(".bin")) {
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
                    oos.writeObject(student);
                }
            } else if (fileName.endsWith(".xml")) {
                xmlMapper.writeValue(new File(fileName), student);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a {@code Student} object from a file in JSON, binary, or XML format based on
     * the file extension.
     *
     * @param fileName The name of the file to load the {@code Student} object from.
     * @return The loaded {@code Student} object.
     */
    public static Student loadStudentFromFile(String fileName) {
        Student student = new Student();
        File file = new File(fileName);
        if (file.exists()) {
            try {
                if (fileName.endsWith(".json")) {
                    student = objectMapper.readValue(
                            file, objectMapper.getTypeFactory().constructType(Student.class));
                } else if (fileName.endsWith(".bin")) {
                    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                        student = (Student) ois.readObject();
                    }
                } else if (fileName.endsWith(".xml")) {
                    student = xmlMapper.readValue(
                            file, xmlMapper.getTypeFactory().constructType(Student.class));
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("File not found \"" + fileName + "\"");
        }
        return student;
    }
}
