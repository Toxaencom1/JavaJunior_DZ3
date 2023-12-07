package com.taxah.jj.dz3;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.*;

public class Student implements Externalizable {
    //region Fields
    @Serial
    private static final long serialVersionUID = 1L;
    private String name;
    private int age;
    private transient double GPA;
    //endregion

    //region Constructors
    public Student() {
    }

    public Student(String name, int age, double GPA) {
        this.name = name;
        this.age = age;
        this.GPA = GPA;
    }
    //endregion

    //region Methods
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(name);
        out.writeInt(age);
//        out.writeDouble(GPA); // не сериализуем
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name = (String) in.readObject();
        age = in.readInt();

    }

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

    //Из-за использования интерфейса "Externalizable" делаем собственный класс сериализации
    @JsonSerialize(using = GPAValueSerializer.class)
    public double getGPA() {
        return GPA;
    }

    //Из-за использования интерфейса "Externalizable" делаем собственный класс десериализации
    @JsonDeserialize(using = GPAValueDeserializer.class)
    public void setGPA(double GPA) {
        this.GPA = GPA;
    }
    //endregion

    //region Nested classes
    public static class GPAValueSerializer extends StdSerializer<Double> {
        public GPAValueSerializer() {
            super(Double.class);
        }

        @Override
        public void serialize(Double value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeNumber(0.0); // Всегда сериализуем значение 0.0
        }
    }


    public static class GPAValueDeserializer extends StdDeserializer<Double> {
        public GPAValueDeserializer() {
            super(Double.class);
        }

        @Override
        public Double deserialize(com.fasterxml.jackson.core.JsonParser p, com.fasterxml.jackson.databind.DeserializationContext ctxt) throws IOException {
            return 0.0; // Всегда десериализуем значение 0.0
        }
    }
    //endregion
}

