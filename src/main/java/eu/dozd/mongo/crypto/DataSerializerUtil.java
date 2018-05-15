package eu.dozd.mongo.crypto;

import java.io.*;

/**
 * Created by Gernot Pansy <gernot.pansy@ut11.net> on 15.05.18.
 */
public final class DataSerializerUtil {

    private DataSerializerUtil() { }

    /**
     * Serializes a object to byte array.
     *
     * @param object the object
     * @return the byte array
     * @throws IOException
     */
    public static byte[] serialize(Object object) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream os = new DataOutputStream(out);
        if (object instanceof Integer) {
            os.writeByte(0);
            os.writeInt((Integer) object);
        } else if (object instanceof Long) {
            os.writeByte(1);
            os.writeLong((Long) object);
        } else if (object instanceof Double) {
            os.writeByte(2);
            os.writeDouble((Double) object);
        } else if (object instanceof Boolean) {
            os.writeByte(3);
            os.writeBoolean((Boolean) object);
        } else if (object instanceof Float) {
            os.writeByte(4);
            os.writeFloat((Float) object);
        } else if (object instanceof Byte) {
            os.writeByte(5);
            os.writeByte((Byte) object);
        } else if (object instanceof Short) {
            os.writeByte(6);
            os.writeShort((Short) object);
        } else if (object instanceof String) {
            os.writeByte(7);
            os.writeUTF((String) object);
        } else if (object instanceof byte[]) {
            os.writeByte(8);
            os.write((byte[]) object);
        } else {
            os.writeByte(255);
            ByteArrayOutputStream objectOut = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(objectOut);
            oos.writeObject(object);
            os.write(objectOut.toByteArray());
        }
        return out.toByteArray();
    }

    /**
     * Deserializes a previously serialized byte array to an object.
     *
     * @param data the serialized byte array
     * @return the deserialized object
     * @throws IOException
     * @throws ClassNotFoundException thrown if the deserialized object doesn't exit in the classloader
     */
    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        DataInputStream is = new DataInputStream(in);
        byte type = is.readByte();
        switch (type) {
            case 0:
                return is.readInt();
            case 1:
                return is.readLong();
            case 2:
                return is.readDouble();
            case 3:
                return is.readBoolean();
            case 4:
                return is.readFloat();
            case 5:
                return is.readByte();
            case 6:
                return is.readShort();
            case 7:
                return is.readUTF();
            case 8:
                return readByte(is);

            default:
                ByteArrayInputStream objectIn = new ByteArrayInputStream(readByte(is));
                ObjectInputStream ois = new ObjectInputStream(objectIn);
                return ois.readObject();
        }
    }

    private static byte[] readByte(DataInputStream is) throws IOException {
        byte[] data = new byte[is.available()];
        is.readFully(data);
        return data;
    }
}
