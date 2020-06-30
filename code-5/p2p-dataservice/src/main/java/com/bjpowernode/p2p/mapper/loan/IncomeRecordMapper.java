package com.bjpowernode.p2p.mapper.loan;


import com.bjpowernode.p2p.model.loan.IncomeRecord;

import java.util.List;

public interface IncomeRecordMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_income_record
     *
     * @mbggenerated Fri Feb 22 16:37:59 CST 2019
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_income_record
     *
     * @mbggenerated Fri Feb 22 16:37:59 CST 2019
     */
    int insert(IncomeRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_income_record
     *
     * @mbggenerated Fri Feb 22 16:37:59 CST 2019
     */
    int insertSelective(IncomeRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_income_record
     *
     * @mbggenerated Fri Feb 22 16:37:59 CST 2019
     */
    IncomeRecord selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_income_record
     *
     * @mbggenerated Fri Feb 22 16:37:59 CST 2019
     */
    int updateByPrimaryKeySelective(IncomeRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_income_record
     *
     * @mbggenerated Fri Feb 22 16:37:59 CST 2019
     */
    int updateByPrimaryKey(IncomeRecord record);

    List<IncomeRecord> selectIncomeRecordByIncomeStatusAndCurds();
}