package util;

import org.h2.util.StringUtils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlUtil {
    private static Pattern linePattern = Pattern.compile("_(\\w)");

    public static String format(String sql){
        return sql;
    }

//    public static void main(String[] args) {
//        String parse = parse("SELECT wwp.WORK_PLAN_ID,  wwp.PRE_WORK_PLAN_ID,  wwp.SF_SORT,  wwp.WORK_SORT,  wwp.WORK_SORT_SUB,  wwp.PLAN_NO,  wwp.PLAN_TYPE,  wwp.PLAN_CYCLE,  wwp.PLAN_YEAR,  wwp.PLAN_MONTH,  wwp.ORG_NO,  wwp.DEPT_NO,  wwp.EMP_NO,  wwp.MAKE_DATE,  wwp.PLAN_STATUS,  wwp.PLAN_NUM,  wwp.FINISH_NUM,  wwp.EXCEPT_NUM,  wwp.APP_NO,  wwp.REMARK,  wwp.PLAN_WEEK,  wwp.PLAN_CONTENT,  wwp.PLAN_SEASON,\twwpd.WPD_ID,  wwpd.OBJ_ID,  wwpd.OBJ_TYPE,  wwpd.VOLT_CODE,  wwpd.MNGN_ORG_NO,  wwpd.MNTN_ORG_NO,  wwpd.ADDR,  wwpd.PLAN_DATE,  wwpd.MAKE_ORG_NO,  wwpd.MAKE_DEPT_NO,  wwpd.MAKE_EMP_NO,  wwpd.EXE_ORG_NO,  wwpd.EXE_DEPT_NO,  wwpd.EXE_EMP_NO,  wwpd.EXE_DATE,  wwpd.COM_RESON,  wwpd.SRC_WPD_ID,  wwpd.TASK_NO FROM w_work_plan_det wwpd , w_work_plan wwp where wwpd.WORK_PLAN_ID=wwp.WORK_PLAN_ID");
//        System.out.println(parse);
//    }

    public static String parse(String sql){
        String clearLowSql = clearSql(toSqlLowerCase(sql));
        String[] strings = cutSql(clearLowSql);
        return parseStrings(strings);

    }

    private static String parseStrings(String[] strings) {
        StringBuffer stringBuffer = new StringBuffer();
        Arrays.stream(strings).forEach(field->{
          stringBuffer.append("private String "+getResultField(field)+"; \n");
        }
        );
        return stringBuffer.toString();
    }

    private static String getResultField(String field) {
        if (StringUtils.isNullOrEmpty(field)){
            return "";
        }
        String trim = field.trim();
        String[] s = trim.split(" ");
        if (s.length>1){
            return  convertCamel(s[s.length-1]);
        }else {
            String[] split = s[0].split("\\.");
            return convertCamel(split[split.length-1]);
        }

    }

    private static String convertCamel(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }


    /**
     * convernt string to lower case
     *
     * @param sql
     * @return
     */
    private static String toSqlLowerCase(String sql){
        return sql.toLowerCase();
    }

    /**
     * remove extra symbol
     *
     * @param sql
     * @return
     */
    private static String clearSql(String sql){
        String replace = sql.replace("`", "");
        return replace.replace("\n"," ");

    }

    /**
     * remove query condition（SELECT,String after WHERE）
     * fetch query variable
     *
     * @param sql
     * @return
     */
    private static String[] cutSql(String sql){
        String[] split = sql.split(" from ");
        String result = split[0].replaceFirst("select", "");
        return result.split(",");
    }
}
