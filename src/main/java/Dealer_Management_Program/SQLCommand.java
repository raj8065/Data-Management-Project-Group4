package Dealer_Management_Program;

public class SQLCommand {

    public enum CommandType {SELECT, DELETE, INSERT};

    private final CommandType type;
    private final String[] tables;
    private final String[] columnNames;
    private final String[] groupBy;
    private final String where;

    public SQLCommand(CommandType t, String[] dbs, String[] colNames, String[] grpBy, String w) {
        this.type = t;
        this.tables = dbs;
        this.columnNames = colNames;
        this.groupBy = grpBy;
        this.where = w;
    }

    private StringBuilder parseArray(StringBuilder sb, String[] array) {

        for(int i = 0; i < array.length; i++)
            if(i != array.length - 1)
                sb.append(array[i]+ ",");
            else
                sb.append(array[i]+ " ");

        return sb;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.type.name() + " ");

        if(this.type == CommandType.INSERT)
            sb.append("INTO ");

        sb = parseArray(sb, this.columnNames);

        sb.append("FROM ");

        sb = parseArray(sb, this.tables);

        if(this.groupBy.length > 0) {
            sb.append("GROUPBY ");

            sb = parseArray(sb, this.groupBy);
        }

        if(!where.equals(""))
            sb.append("WHERE " + this.where);

        return sb.toString();
    }
}
