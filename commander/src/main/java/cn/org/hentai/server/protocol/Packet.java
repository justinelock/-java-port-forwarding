package cn.org.hentai.server.protocol;

import cn.org.hentai.server.protocol.command.Command;
import cn.org.hentai.server.util.ByteUtils;
import cn.org.hentai.server.util.DES;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by Expect on 2018/1/25.
 */
public final class Packet {
    public static final int ENCRYPT_TYPE_NONE = 0x00;       // 数据体不加密
    public static final int ENCRYPT_TYPE_DES = 0x01;        // 数据体通过DES加密

    // 读取一整个包
    public static byte[] read(InputStream reader) throws Exception {
        return read(reader, false);
    }

    public static byte[] read(InputStream reader, boolean wait) throws Exception {
        ByteArrayOutputStream buff = new ByteArrayOutputStream(512);
        while (wait && reader.available() < 13) Thread.sleep(10);
        if (reader.available() < 13) return null;
        byte[] data = new byte[13];

        // 读取包头
        reader.read(data);

        // 协议头检测
        if ((data[0] & 0xff) != 0xfa || (data[1] & 0xff) != 0xfa || (data[2] & 0xff) != 0xfa)
            throw new RuntimeException("错误的协议头");

        // 数据体长度
        int bodyLength = ByteUtils.getInt(data, 3, 4);
        buff.write(data);

        // 读取数据体
        data = new byte[bodyLength];
        int recv = reader.read(data);
        if (bodyLength != recv) throw new RuntimeException("无法从流中读取足够的数据");
        buff.write(data);
        return buff.toByteArray();
    }

    public static byte[] create(int hostId, int encryptType, Command command, String accesstoken) throws Exception {
        return create(hostId, encryptType, command.getCode(), command.getBytes(), accesstoken);
    }

    /*
     * FA FA FA 协议头
     * 00 00 00 00 加密后的数据体长度
     * 00 00 00 00 主机ID
     * 00 00 指令，最高2位用于描述加密类型，01表示AES加密
     * ...... AES加密后的数据体
     */
    public static byte[] create(int hostId, int encryptType, int command, byte[] data, String accesstoken) throws Exception {
        byte[] encryptedData = null;
        if (encryptType == 0x00) ;
        else if (encryptType == 0x01) encryptedData = DES.encrypt(data, accesstoken);
        else throw new RuntimeException("不支持的加密类型: " + encryptType);

        int encryptedDataLength = encryptedData.length;
        byte[] packet = new byte[3 + 4 + 4 + 2 + encryptedDataLength];
        packet[0] = (byte) 0xfa;
        packet[1] = (byte) 0xfa;
        packet[2] = (byte) 0xfa;
        System.arraycopy(ByteUtils.toBytes(encryptedDataLength), 0, packet, 3, 4);
        System.arraycopy(ByteUtils.toBytes(hostId), 0, packet, 7, 4);

        command &= 0x3ff;
        command |= ((encryptType & 0x03) << 14);
        packet[11] = (byte) ((command >> 8) & 0xff);
        packet[12] = (byte) (command & 0xff);
        System.arraycopy(encryptedData, 0, packet, 13, encryptedDataLength);
        return packet;
    }

    public static int getDataLength(byte[] packet) {
        return ByteUtils.getInt(packet, 3, 4);
    }

    public static int getHostId(byte[] packet) {
        return ByteUtils.getInt(packet, 7, 4);
    }

    public static int getCommand(byte[] packet) {
        byte h = packet[11];
        byte l = packet[12];
        return (((h & 0xff) << 8) | (l & 0xff)) & 0x3ff;
    }

    public static byte[] getData(byte[] packet, String accesstoken) throws Exception {
        int encryptType = (packet[11] >> 6) & 0x03;
        int dataLength = getDataLength(packet);
        byte[] data = new byte[dataLength];
        System.arraycopy(packet, 13, data, 0, dataLength);
        if (accesstoken != null && encryptType == 0x00)
            throw new RuntimeException("未加密的数据包: " + ByteUtils.toString(packet));
        if (encryptType == 0x00) return data;
        else if (encryptType == 0x01) return DES.decrypt(data, accesstoken);
        else throw new RuntimeException("不支持的加密类型: " + encryptType);
    }

    public static void main(String[] args) throws Exception {
        byte[] data = ByteUtils.parse("11 22 33 44 55 66 77 88 99 00 aa bb cc dd ee ff");
        String accesstoken = "pKqTIBgMwSp9JdFC4xLyQ34R5dRcZCYZ861VNAPPIcOwNQi5wwNdqo3vB59kHlXi";

        byte[] packet = create(1212, 0x01, 0x132, data, accesstoken);
        System.out.println(ByteUtils.toString(packet));

        System.out.println("Length : " + getDataLength(packet));
        System.out.println("HostId : " + getHostId(packet));
        System.out.println("Command: " + getCommand(packet));
        System.out.println("Data   : " + ByteUtils.toString(getData(packet, accesstoken)));
    }
}
