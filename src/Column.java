/**
 * Created by Dmitry on 26.11.2016.
 */
public class Column {
    private int ordinal = -1;
    private String name = "";
    private boolean hasForeignKey = false;
    private String foreignTableName = "";
    private String foreignColumnName = "";
    private boolean isInt = false;

    public Column(int ordinal, String name, boolean isInt) {
        this.ordinal = ordinal;
        this.name = name;
        this.isInt = isInt;
    }

    public void setForeignTableName(String foreignTableName) {
        this.foreignTableName = foreignTableName;
    }

    public void setForeignColumnName(String foreignColumnName) {
        this.foreignColumnName = foreignColumnName;
    }

    public void setHasForeignKey(boolean hasForeignKey) {

        this.hasForeignKey = hasForeignKey;
    }

    public String getForeignColumnName() {
        return foreignColumnName;
    }

    public String getForeignTableName() {
        return foreignTableName;
    }

    public int getOrdinal() {

        return ordinal;
    }

    public String getName() {

        return name;
    }

    public boolean isInt() {
        return isInt;
    }
}
