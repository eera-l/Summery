import static org.junit.Assert.*;

/**
 * These tests operate on the database, be aware of entries in the database before running the tests.
 * */

public class ConnectorTest {

    @org.junit.Test
    public void searchByID() {
        System.out.println("searchByID");
        Connector connector = new Connector();
        String ip_address = connector.searchByID("ip_address","'462742b4-6891-4035-8951-c3d4aeadc950'");
        assertEquals(ip_address,"ip6-localhost/0:0:0:0:0:0:0:1:58266");
    }

    @org.junit.Test
    public void searchByIPAddress() {
        System.out.println("searchByIPAddress");
        Connector connector = new Connector();
        String iduser = connector.searchByIPAddress("iduser","ip6-localhost/0:0:0:0:0:0:0:1:58266");
        assertEquals(iduser,"462742b4-6891-4035-8951-c3d4aeadc950");
    }

    @org.junit.Test
    public void updateById() {
        System.out.println("updateByID");
        Connector connector = new Connector();
        connector.updateById("ip_address","'ip6-localhost/0:0:0:0:0:0:0:2:58266'","'462742b4-6891-4035-8951-c3d4aeadc950'");
        String ip_address = connector.searchByID("ip_address","'462742b4-6891-4035-8951-c3d4aeadc950'");
        assertEquals(ip_address,"ip6-localhost/0:0:0:0:0:0:0:2:58266");
    }

    @org.junit.Test
    public void updateByIpAddress() {
        System.out.println("updateByIPAddress");
        Connector connector = new Connector();
        connector.updateByIpAddress("ip_address","'new IP'","ip6-localhost/0:0:0:0:0:0:0:2:58266");
        String ip = connector.searchByIPAddress("ip_address","new IP");
        assertEquals(ip,"new IP");
    }

    @org.junit.Test
    public void insert() {
        System.out.println("insert");
        Connector connector = new Connector();
        connector.insert("'userID', 'IP_Address'");
        String iduser = connector.searchByIPAddress("iduser","IP_Address");
        assertEquals(iduser,"userID");
    }

    @org.junit.Test
    public void delete() {
        System.out.println("delete");
        Connector connector = new Connector();
        connector.delete("iduser='userID'");
    }

    @org.junit.Test
    public void addCookie() {
        System.out.println("addCookie");
        Connector connector = new Connector();
        connector.addCookie("462742b4-0000-4035-8951-c3d4aeadc950","ip6-localhost/0:0:0:0:0:0:0:2:58266","Cookie");

    }
}