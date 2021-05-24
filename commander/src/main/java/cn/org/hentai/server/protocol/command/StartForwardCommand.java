package cn.org.hentai.server.protocol.command;

import cn.org.hentai.server.util.ByteUtils;
import cn.org.hentai.server.util.NonceStr;

/**
 * Created by Expect on 2018/3/5.
 */
public class StartForwardCommand extends Command {
    int sequenceId = 0;
    int hostPort = 0;
    String hostIp = null;
    String nonce = null;

    public StartForwardCommand(int sequenceId, String hostIp, int hostPort, String nonce) {
        this.setCode(Command.CODE_START_FORWARD);
        this.sequenceId = sequenceId;
        this.hostIp = hostIp;
        this.hostPort = hostPort;
        this.nonce = nonce;
    }

    @Override
    public byte[] getBytes() {
        // 4字节序号
        // 4字节端口号
        byte[] data = new byte[8 + 64 + 1 + hostIp.length()];
        System.arraycopy(ByteUtils.toBytes(this.sequenceId), 0, data, 0, 4);
        System.arraycopy(ByteUtils.toBytes(this.hostPort), 0, data, 4, 4);
        System.arraycopy(this.nonce.getBytes(), 0, data, 8, 64);
        data[8 + 64] = (byte) (hostIp.length() & 0xff);
        System.arraycopy(hostIp.getBytes(), 0, data, 8 + 64 + 1, hostIp.length());
        return data;
    }
}
