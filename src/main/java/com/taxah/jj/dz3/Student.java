package com.taxah.jj.dz3;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.*;

/**
 * The 'Student' class implements the 'Externalizable' interface for custom serialization.
 * It represents a student with fields for name, age, and GPA.
 *
 * @author Anton Takhayev
 * @version 1.0
 * @since 07-12-2023
 */
public class Student implements Externalizable {
    //region Fields
    /**
     * Serial version UID for class versioning.
     */
    @Serial
    private static final long serialVersionUID = 1L;
    private String name;
    private int age;
    /**
     * The GPA (Grade Point Average) of the student (transient field, not serialized).
     */
    private transient double GPA;
    //endregion

    //region Constructors

    /**
     * Default constructor for the {@code Student} class.
     */
    public Student() {
    }

    /**
     * Parameterized constructor for the {@code Student} class.
     *
     * @param name The name of the student.
     * @param age  The age of the student.
     * @param GPA  The GPA of the student.
     */
    public Student(String name, int age, double GPA) {
        this.name = name;
        this.age = age;
        this.GPA = GPA;
    }
    //endregion

    //region Methods

    /**
     * Writes the object's state to an external stream.
     *
     * @param out The object output stream.
     * @throws IOException If an I/O error occurs during the write operation.
     */
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(name);
        out.writeInt(age);
//        out.writeDouble(GPA); // не сериализуем
    }

    /**
     * Reads the object's state from an external stream.
     *
     * @param in The object input stream.
     * @throws IOException            If an I/O error occurs during the read operation.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name = (String) in.readObject();
        age = in.readInt();

    }

    /**
     * Returns a string representation of the {@code Student} object.
     *
     * @return A string representation of the object.
     */
    @Override
    public String toString() {
        return "Student, " +
                "name = '" + name + '\'' +
                ", age = " + age +
                ", GPA = " + GPA +
                '}';
    }
    //endregion

    //region Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    /**
     * Gets the GPA of the student.
     *
     * @return The GPA of the student.
     */
    //Из-за использования интерфейса "Externalizable" делаем собственный класс сериализации
    @JsonSerialize(using = GPAValueSerializer.class)
    public double getGPA() {
        return GPA;
    }

    /**
     * Sets the GPA of the student.
     *
     * @param GPA The new GPA for the student.
     */
    //Из-за использования интерфейса "Externalizable" делаем собственный класс десериализации
    @JsonDeserialize(using = GPAValueDeserializer.class)
    public void setGPA(double GPA) {
        this.GPA = GPA;
    }
    //endregion

    //region Nested classes
    /**
     * Custom serializer for the GPA field.
     */
    public static class GPAValueSerializer extends StdSerializer<Double> {
        public GPAValueSerializer() {
            super(Double.class);
        }

        @Override
        public void serialize(Double value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeNumber(0.0); // Всегда сериализуем в значение 0.0
        }
    }

    /**
     * Custom deserializer for the GPA field.
     */
    public static class GPAValueDeserializer extends StdDeserializer<Double> {
        public GPAValueDeserializer() {
            super(Double.class);
        }

        @Override
        public Double deserialize(com.fasterxml.jackson.core.JsonParser p, com.fasterxml.jackson.databind.DeserializationContext ctxt) throws IOException {
            return 0.0; // Всегда десериализуем в значение 0.0
        }
    }
    //endregion
}

