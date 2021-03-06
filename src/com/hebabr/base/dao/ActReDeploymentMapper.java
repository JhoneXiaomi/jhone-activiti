package com.hebabr.base.dao;

import com.hebabr.model.ActReDeployment;
import com.hebabr.model.ActReDeploymentExample;
import java.util.List;

import org.springframework.stereotype.Repository;


@Repository
public interface ActReDeploymentMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_re_deployment
     *
     * @mbg.generated Thu Feb 01 17:57:25 CST 2018
     */
    long countByExample(ActReDeploymentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_re_deployment
     *
     * @mbg.generated Thu Feb 01 17:57:25 CST 2018
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_re_deployment
     *
     * @mbg.generated Thu Feb 01 17:57:25 CST 2018
     */
    int insert(ActReDeployment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_re_deployment
     *
     * @mbg.generated Thu Feb 01 17:57:25 CST 2018
     */
    int insertSelective(ActReDeployment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_re_deployment
     *
     * @mbg.generated Thu Feb 01 17:57:25 CST 2018
     */
    List<ActReDeployment> selectByExample(ActReDeploymentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_re_deployment
     *
     * @mbg.generated Thu Feb 01 17:57:25 CST 2018
     */
    ActReDeployment selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_re_deployment
     *
     * @mbg.generated Thu Feb 01 17:57:25 CST 2018
     */
    int updateByPrimaryKeySelective(ActReDeployment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_re_deployment
     *
     * @mbg.generated Thu Feb 01 17:57:25 CST 2018
     */
    int updateByPrimaryKey(ActReDeployment record);
}