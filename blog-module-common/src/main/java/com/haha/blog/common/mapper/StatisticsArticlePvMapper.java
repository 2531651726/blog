package com.haha.blog.common.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.haha.blog.common.domain.dos.StatisticsArticlePvDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 统计表 - 文章 PV (访问量) Mapper 接口
 * </p>
 *
 * @author li
 * @since 2026-06-27
 */
@Mapper
public interface StatisticsArticlePvMapper extends BaseMapper<StatisticsArticlePvDO> {

    /**
     * 按日累加 PV, 若当天记录不存在则插入新记录
     * @param pvDate 统计日期
     * @param increment 增量
     */
    default void incrementPvCount(LocalDate pvDate, Long increment) {
        if (pvDate == null || increment == null || increment <= 0) {
            return;
        }
        int affected = update(null, Wrappers.<StatisticsArticlePvDO>lambdaUpdate()
                .setSql("pv_count = pv_count + " + increment)
                .eq(StatisticsArticlePvDO::getPvDate, pvDate));
        // 当天无记录存在则新增
        if (affected == 0) {
            StatisticsArticlePvDO pvDO = new StatisticsArticlePvDO()
                    .setPvDate(pvDate)
                    .setPvCount(increment);
            insert(pvDO);
        }
    }

    // 查询指定日期 7 天内 pv 数据
    default List<StatisticsArticlePvDO> queryLatestWeekPvRecords(LocalDate date) {
        return selectList(Wrappers.<StatisticsArticlePvDO>lambdaQuery()
                .ge(StatisticsArticlePvDO::getPvDate, date.minusDays(6)));
    }
}
