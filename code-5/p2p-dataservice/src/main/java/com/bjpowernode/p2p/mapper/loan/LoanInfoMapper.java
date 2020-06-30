package com.bjpowernode.p2p.mapper.loan;


import com.bjpowernode.p2p.model.loan.LoanInfo;

import java.util.List;
import java.util.Map;

public interface LoanInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_loan_info
     *
     * @mbggenerated Fri Feb 22 16:37:59 CST 2019
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_loan_info
     *
     * @mbggenerated Fri Feb 22 16:37:59 CST 2019
     */
    int insert(LoanInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_loan_info
     *
     * @mbggenerated Fri Feb 22 16:37:59 CST 2019
     */
    int insertSelective(LoanInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_loan_info
     *
     * @mbggenerated Fri Feb 22 16:37:59 CST 2019
     */
    LoanInfo selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_loan_info
     *
     * @mbggenerated Fri Feb 22 16:37:59 CST 2019
     */
    int updateByPrimaryKeySelective(LoanInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_loan_info
     *
     * @mbggenerated Fri Feb 22 16:37:59 CST 2019
     */
    int updateByPrimaryKey(LoanInfo record);
    /*
    *获取历史年化平均收益率
    *
    *
    */
    Double selectHistoryAverageRate();
    /**
     * 根据产品类型分页查询产品信息
     *
     *
     * **/

    List<LoanInfo> selectLoanInfoByPage(Map<String, Object> map);

    Long selectTotal(Map<String, Object> map);

    int updateleftPrductMoneyByLoanId(Map<String, Object> map);

    List<LoanInfo> selectLoanInfoListByProductStatus(int productStatus);
}