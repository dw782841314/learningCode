package com.hdkj.modules.JW.dao;

import com.hdkj.datasources.annotation.DataSource;
import com.hdkj.modules.JW.entity.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface NewApiDao {

    @Select("<script>" +
            " select * from ( select A.*,ROWNUM RN from (" +
            " SELECT cust_id AS  CUST_ID, cons_no AS CONS_NO, cons_name AS CONS_NAME, org_no AS ORG_CODE," +
            " (SELECT org_name" +
            " FROM  o_org" +
            " WHERE org_no = a.org_no) AS ORG_NAME， (SELECT TRIM(NAME)" +
            " FROM  p_code" +
            " WHERE code_type = 'tradeCode'" +
            " AND VALUE = a.trade_code) AS  TRADE_CODE， a.elec_addr AS ELEC_ADDR， (SELECT TRIM(NAME)" +
            " FROM  p_code" +
            " WHERE code_type =" +
            " 'elecTypeCode'" +
            " AND VALUE =" +
            " a.elec_type_code) AS ELEC_TYPE_CODE," +
            " (SELECT TRIM(NAME)" +
            " FROM  p_code" +
            " WHERE code_type = 'psVoltCode'" +
            " AND VALUE = a.volt_code) AS VOLT_CODE, a.build_date AS BUILD_DATE," +
            " a.ps_date AS PS_DATE， a.cancel_date AS CANCEL_DATE" +
            " FROM  c_cons a" +
            " WHERE 1=1 " +
            " <if test = 'CONS_NAME != null'>" +
            " AND cons_name LIKE concat(concat('%',#{CONS_NAME}),'%')" +
            " </if>" +
            " <if test = 'ELEC_ADDR != null'> " +
            " AND elec_addr LIKE concat(concat('%',#{ELEC_ADDR}),'%')" +
            " </if>" +
            " <if test = 'CERT_NO != null'>" +
            " AND EXISTS (SELECT 1" +
            " FROM  c_cert" +
            " WHERE cust_id = a.cust_id" +
            " AND cert_no LIKE concat(concat('%',#{CERT_NO}),'%')" +
            " AND cert_type_code = '01'" +
            " AND org_no = substr(a.org_no, 1, 5))" +
            " </if>" +
            " <if test = 'MOBILE != null'>" +
            " AND EXISTS (SELECT 1" +
            " FROM  c_contact" +
            " WHERE cust_id = a.cust_id" +
            " AND mobile LIKE concat(concat('%',#{MOBILE}),'%')" +
            " AND org_no = substr(a.org_no, 1, 5))" +
            "</if>" +
            ") A where ROWNUM &lt;= #{PAGE} " +
            " )" +
            " where RN &gt;= #{PAGE_NO}" +
            " </script>")
    @DataSource(name = "second")
    List<Map<String,Object>> queryUserProfiles(@Param("CONS_NAME") String CONS_NAME, @Param("CERT_NO") String CERT_NO,
                                         @Param("MOBILE") String MOBILE, @Param("ELEC_ADDR") String ELEC_ADDR,
                                         @Param("PAGE_NO") int PAGE_NO, @Param("PAGE") int PAGE);//总信息


    @Select("<script>" +
            " SELECT cust_id AS  CUST_ID, cons_no AS CONS_NO, cons_name AS CONS_NAME, org_no AS ORG_CODE," +
            " (SELECT org_name" +
            " FROM  o_org" +
            " WHERE org_no = a.org_no) AS ORG_NAME， (SELECT TRIM(NAME)" +
            " FROM  p_code" +
            " WHERE code_type = 'tradeCode'" +
            " AND VALUE = a.trade_code) AS  TRADE_CODE， a.elec_addr AS ELEC_ADDR， (SELECT TRIM(NAME)" +
            " FROM  p_code" +
            " WHERE code_type =" +
            " 'elecTypeCode'" +
            " AND VALUE =" +
            " a.elec_type_code) AS ELEC_TYPE_CODE," +
            " (SELECT TRIM(NAME)" +
            " FROM  p_code" +
            " WHERE code_type = 'psVoltCode'" +
            " AND VALUE = a.volt_code) AS VOLT_CODE, a.build_date AS BUILD_DATE," +
            " a.ps_date AS PS_DATE， a.cancel_date AS CANCEL_DATE" +
            " FROM  c_cons a" +
            " WHERE 1=1 " +
            " <if test = 'CONS_NAME != null'>" +
            " AND cons_name LIKE concat(concat('%',#{CONS_NAME}),'%')" +
            " </if>" +
            " <if test = 'ELEC_ADDR != null'> " +
            " AND elec_addr LIKE concat(concat('%',#{ELEC_ADDR}),'%')" +
            " </if>" +
            " <if test = 'CERT_NO != null'>" +
            " AND EXISTS (SELECT 1" +
            " FROM  c_cert" +
            " WHERE cust_id = a.cust_id" +
            " AND cert_no LIKE concat(concat('%',#{CERT_NO}),'%')" +
            " AND cert_type_code = '01'" +
            " AND org_no = substr(a.org_no, 1, 5))" +
            " </if>" +
            " <if test = 'MOBILE != null'>" +
            " AND EXISTS (SELECT 1" +
            " FROM  c_contact" +
            " WHERE cust_id = a.cust_id" +
            " AND mobile LIKE concat(concat('%',#{MOBILE}),'%')" +
            " AND org_no = substr(a.org_no, 1, 5))" +
            "</if>"+
            " </script>")
    @DataSource(name = "second")
    List<Map<String,Object>> queryCount(@Param("CONS_NAME") String CONS_NAME, @Param("CERT_NO") String CERT_NO,
                                               @Param("MOBILE") String MOBILE, @Param("ELEC_ADDR") String ELEC_ADDR);//总信息

    @Select("SELECT (SELECT bank_name" +
            " FROM  a_bank" +
            " WHERE bank_code = a.bank_code) AS BANK_NAME, a.acct_name AS ACCT_NAME, a.bank_acct AS BANK_ACCT" +
            " FROM  c_bank_acct a" +
            " WHERE c_a_acct_id IN (SELECT acct_id" +
            " FROM  c_acct" +
            " WHERE cust_id = (select t.cust_id from c_cons t where t.cons_no = #{CONS_NO})" +
            " and org_no = substr(#{ORG_NO},1,5))")
    @DataSource(name = "second")
    List<Map<String,Object>> queryAccountInfo(@Param("CONS_NO") String CONS_NO, @Param("ORG_NO") String ORG_NO);//银行信息

    @Select("SELECT contact_name AS CONTACT_NAME, mobile AS MOBILE" +
            "  FROM  c_contact" +
            " WHERE cust_id = (select t.cust_id from c_cons t where t.cons_no = #{CONS_NO})" +
            " and org_no = substr(#{ORG_NO},1,5)" +
            " order by contact_prio")
    @DataSource(name = "second")
    List<Map<String,Object>> queryMobile(@Param("CONS_NO") String CONS_NO, @Param("ORG_NO") String ORG_NO);//联系信息

    @Select("SELECT a.pro_title AS PRO_TITLE, a.end_date AS END_DATE" +
            " FROM indywf_instances_cur a,  arc_s_app b" +
            " WHERE a.item_key = b.app_no" +
            " AND b.cons_no = #{CONS_NO}" +
            " ORDER BY b.cons_no, a.end_date")
    @DataSource(name = "second")
    List<Map<String,Object>> queryAlteration(@Param("CONS_NO") String CONS_NO);//查询变更信息

    @Select("SELECT " +
            "(SELECT NAME FROM p_code " +
            "WHERE code_type = 'certType' " +
            "AND VALUE = b.cert_type_code) as CERT_TYPE_CODE, b.cert_no as CERT_NO " +
            "FROM c_cons a, c_cert b " +
            "WHERE a.cons_no =#{CONS_NO} " +
            "AND a.cust_id = b.cust_id")
    @DataSource(name = "second")
    List<Map<String,Object>> getCertificateInfo(@Param("CONS_NO") String CONS_NO);//证件信息

    @Select("SELECT  t_pq AS T_PQ,rcvbl_ym AS RCVBL_YM" +
            " FROM  arc_a_rcvbl_flow" +
            " WHERE cons_no = #{CONS_NO}" +
            " AND org_no = #{ORG_NO}" +
            " and rcvbl_ym between #{START_YM} and #{END_YM}" +
            " union all" +
            " SELECT t_pq AS T_PQ,rcvbl_ym AS RCVBL_YM" +
            " FROM  a_rcvbl_flow" +
            " WHERE cons_no = #{CONS_NO}" +
            " AND org_no = #{ORG_NO}" +
            " and rcvbl_ym between #{START_YM} and #{END_YM}" +
            " ORDER BY rcvbl_ym")
    @DataSource(name = "second")
    List<Map<String,Object>> queryElecQuery(@Param("CONS_NO") String CONS_NO, @Param("ORG_NO") String ORG_NO,
                                   @Param("START_YM") String START_YM, @Param("END_YM") String END_YM);//应收电量

    @Select("SELECT a.rcv_amt AS RCV_AMT," +
            " CASE" +
            " WHEN a.dept_no LIKE concat('4','%') THEN" +
            " (SELECT org_name" +
            " FROM  o_org" +
            " WHERE org_no = a.dept_no)" +
            " WHEN a.dept_no NOT LIKE concat('4','%') THEN" +
            " (SELECT bank_name" +
            " FROM  a_bank" +
            " WHERE bank_code = a.dept_no)" +
            " END AS DEPT_NO," +
            " (SELECT org_name" +
            " FROM  o_org" +
            " WHERE org_no = a.rcv_org_no) AS RCV_ORG_NO," +
            " (SELECT NAME" +
            " FROM  p_code" +
            " WHERE code_type = 'payMode'" +
            " AND VALUE = a.pay_mode) AS PAY_MODE, charge_date AS CHARGE_DATE" +
            " FROM  a_pay_flow a" +
            " WHERE cons_no = #{CONS_NO}" +
            " AND org_no = #{ORG_NO}" +
            " AND charge_ym BETWEEN #{START_YM} AND #{END_YM}" +
            " AND rcv_amt <> 0" +
            " ORDER BY charge_date")
    @DataSource(name = "second")
    List<Map<String,Object>> queryPaymentRec(@Param("CONS_NO") String CONS_NO, @Param("ORG_NO") String ORG_NO,
                                             @Param("START_YM") String START_YM, @Param("END_YM") String END_YM);//缴费记录



    List<DailyElectricity> queryDailyElectricity(@Param("CONS_NO") String CONS_NO, @Param("ORG_NO") String ORG_NO,
                                                 @Param("START_YM") String START_YM, @Param("END_YM") String END_YM);//每日电量信息
}
