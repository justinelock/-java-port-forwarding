package cn.org.hentai.server.dao;

import cn.org.hentai.server.model.Port;
import cn.org.hentai.server.util.db.DBAccess;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Expect on 2018/1/25.
 */
@Repository
public class PortDAO extends DBAccess {
    public Port getById(int id) {
        return select().byId(id).query(Port.class);
    }

    // 添加新的端口转发
    public int save(Port port) {
        int id = insertInto().valueWith(port).save();
        port.setId(id);
        return id;
    }

    // 删除端口转发
    public int delete(Port port) {
        return execute("delete from ports where id = ?", port.getId());
    }

    public Port getByPort(int port) {
        return select()
                .where(clause("port = ?", port))
                .query(Port.class);
    }

    // 修改端口设置
    public long update(Port port) {
        return update().valueWith(port).byId().execute();
    }

    @Override
    public String[] configureFields() {
        return new String[]{"id", "name", "user_id", "host_id", "listen_port",
                "connect_timeout", "host_ip", "host_port", "state", "create_time",
                "last_active_time", "so_timeout", "concurrent_connections"};
    }

    @Override
    public String configureTableName() {
        return "ports";
    }

    public List<Port> list(int userId, int hostId) {
        return select()
                .where(clause("user_id = ?", (userId)).and("host_id = ?", (hostId)))
                .queryForList(Port.class);
    }

    public List<Port> listAll() {
        return select()
                .where(clause("state = ?", 1))
                .queryForList(Port.class);
    }
}
