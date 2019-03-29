package com.hdkj.modules.JW.dao;

import com.hdkj.datasources.annotation.DataSource;
import com.hdkj.modules.JW.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface ElecInfoDao {

    @Select("SELECT cons_no as CONS_NO, cons_name as CONS_NAME, " +
            "(select org_name from o_org where org_no = a.org_no) as ORG_NAME, " +
            "(SELECT TRIM(NAME) FROM p_code " +
            "WHERE code_type = 'tradeCode' " +
            "AND VALUE = a.trade_code) as TRADE_CODE, a.elec_addr as ELEC_ADDR, " +
            "(SELECT TRIM(NAME) FROM p_code " +
            "WHERE code_type = 'elecTypeCode' AND VALUE = a.elec_type_code) as ELEC_TYPE_CODE, " +
            "(SELECT TRIM(NAME) FROM p_code " +
            "WHERE code_type = 'psVoltCode' " +
            "AND VALUE = a.volt_code) as VOLT_CODE, a.build_date as BUILD_DATE," +
            "a.ps_date as PS_DATE, a.cancel_date as CANCEL_DATE " +
            "FROM c_cons a " +
            "WHERE cons_no = #{CONS_NO}")
    @DataSource(name = "second")
    UserProfiles getUserProfiles(@Param("CONS_NO") String cons_no);

    @Select("SELECT " +
            "acct_name as ACCT_NAME, " +
            "bank_acct as BANK_ACCT " +
            "FROM c_cons a, c_acct b, c_bank_acct c " +
            "WHERE a.cons_no = #{CONS_NO} " +
            "AND a.cust_id = b.cust_id " +
            "AND b.acct_id = c.c_a_acct_id")
    @DataSource(name = "second")
    List<AccountInfo> getAccountInfo(@Param("CONS_NO") String cons_no);

    @Select("SELECT mobile as MOBILE " +
            "FROM c_cons a, c_contact b " +
            " WHERE a.cons_no =#{CONS_NO} " +
            "AND a.cust_id = b.cust_id " +
            "AND b.mobile IS NOT NULL")
    @DataSource(name = "second")
    List<MobileInfo> getPhone(@Param("CONS_NO") String cons_no);

    @Select("SELECT " +
            "(SELECT NAME FROM p_code " +
            "WHERE code_type = 'certType' " +
            "AND VALUE = b.cert_type_code) as CERT_TYPE_CODE, b.cert_no as CERT_NO " +
            "FROM c_cons a, c_cert b " +
            "WHERE a.cons_no =#{CONS_NO} " +
            "AND a.cust_id = b.cust_id")
    @DataSource(name = "second")
    List<CertificateInfo> getCertificateInfo(@Param("CONS_NO") String cons_no);

    @Select("SELECT a.pro_title as PRO_TITLE, a.end_date as END_DATE " +
            " FROM indywf_instances_cur a,arc_s_app b " +
            " WHERE a.item_key = b.app_no " +
            " and b.cons_no =#{CONS_NO} " +
            " order by b.cons_no,a.end_date")
    @DataSource(name = "second")
    List<Alteration> getAlteration(@Param("CONS_NO") String cons_no);

    @Select("SELECT cons_no, rcvbl_ym as RCVBL_YM, t_pq as T_PQ " +
            "FROM arc_a_rcvbl_flow " +
            "WHERE cons_no = #{CONS_NO} " +
            "AND org_no = #{ORG_NO} " +
            "union all " +
            "SELECT cons_no, rcvbl_ym as RCVBL_YM, t_pq as T_PQ " +
            "FROM a_rcvbl_flow " +
            "WHERE cons_no = #{CONS_NO} " +
            "AND org_no = #{ORG_NO} " +
            "ORDER BY cons_no,rcvbl_ym")
    @DataSource(name = "second")
    List<ElecQuery> getElecQuery(@Param("CONS_NO") String cons_no, @Param("ORG_NO") String org_no);

    @Select("SELECT a.rcv_amt as RCV_AMT, " +
            "CASE " +
            "WHEN a.dept_no LIKE '4%' THEN " +
            "(SELECT org_name " +
            "FROM o_org " +
            "WHERE org_no = a.dept_no) " +
            "WHEN a.dept_no NOT LIKE '4%' THEN " +
            "(SELECT bank_name " +
            "FROM a_bank " +
            "WHERE bank_code = a.dept_no) " +
            "END DEPT_NO, " +
            "(SELECT org_name " +
            "FROM o_org " +
            "WHERE org_no = a.rcv_org_no) as RCV_ORG_NO, " +
            "(SELECT NAME " +
            "FROM p_code " +
            "WHERE code_type = 'payMode' " +
            "AND VALUE = a.pay_mode) as PAY_MODE, charge_date as CHARGE_DATE " +
            "FROM a_pay_flow a " +
            "WHERE cons_no = #{CONS_NO} " +
            "AND org_no = #{ORG_NO} " +
            "AND rcv_amt <> 0 " +
            " ORDER BY cons_no, charge_date")
    @DataSource(name = "second")
    List<PaymentRec> getPaymentRec(@Param("CONS_NO") String cons_no, @Param("ORG_NO") String org_no);


    @Select("SELECT cust_id as CUST_ID , cons_no as CONS_NO, org_no as ORG_NO " +
            " FROM c_cons " +
            " WHERE cons_name = #{CONS_NAME} " +
            " AND cust_id IN (SELECT cust_id " +
            " FROM c_cert " +
            " WHERE cert_no = #{CERT_NO})")
    @DataSource(name = "second")
    List<Map<String, String>> getCust(@Param("CONS_NAME") String cons_name, @Param("CERT_NO") String cert_no);

}