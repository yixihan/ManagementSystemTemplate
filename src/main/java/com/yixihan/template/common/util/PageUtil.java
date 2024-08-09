package com.yixihan.template.common.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixihan.template.vo.req.base.PageReq;
import com.yixihan.template.vo.resp.base.PageVO;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页工具
 *
 * @author yixihan
 * @date 2024-05-21 15:26
 */
@SuppressWarnings("all")
public class PageUtil {

    private PageUtil() {
    }

    /**
     * {@code Page<K> -> Page<V>}
     *
     * @param K      原始实体类
     * @param V      目标实体类
     * @param page   要转换的{@link Page}分页对象
     * @param mapper 实体类转换方法
     * @return {@link Page}
     */
    public static <K, V> Page<V> convertTo(Page<K> page, Function<? super K, ? extends V> mapper) {
        Page<V> newPage = new Page<>();
        BeanUtil.copyProperties(page, newPage, getCopyOption());
        newPage.setRecords(page.getRecords().stream().map(mapper).collect(Collectors.toList()));
        return newPage;
    }

    /**
     * {@code IPage<K> -> Page<V>}
     *
     * @param K      原始实体类
     * @param V      目标实体类
     * @param page   要转换的{@link IPage}分页对象
     * @param mapper 实体类转换方法
     * @return {@link Page}
     */
    public static <K, V> Page<V> convertTo(IPage<K> page, Function<? super K, ? extends V> mapper) {
        Page<V> newPage = new Page<>();
        BeanUtil.copyProperties(page, newPage, getCopyOption());
        newPage.setRecords(page.getRecords().stream().map(mapper).collect(Collectors.toList()));
        return newPage;
    }

    /**
     * {@code IPage<T> -> PageVO<T>}
     *
     * @param T    实体类
     * @param page 要转换的{@link IPage}分页对象
     * @return {@link PageVO}
     */
    public static <T> PageVO<T> pageToPageVO(IPage<T> page) {
        return new PageVO<>(page.getCurrent(), page.getTotal(), page.getSize(), page.getPages(), page.getRecords());
    }


    /**
     * {@code IPage<T> -> PageVO<R>}
     *
     * @param T       原始实体类
     * @param R       目标实体类
     * @param page    要转换的{@link IPage}分页对象
     * @param convert 实体类转换方法
     * @return {@link PageVO}
     */
    public static <T, R> PageVO<R> pageToPageVO(IPage<T> page, Function<T, R> convert) {
        List<T> records = page.getRecords();
        List<R> rList = records == null ? null : records.stream().map(convert).collect(Collectors.toList());
        return new PageVO<>(page.getCurrent(), page.getTotal(), page.getSize(), page.getPages(), rList);
    }

    /**
     * 自定义分页请求参数转为 mybatis plus 分页请求参数
     *
     * @param <T> 目标类
     * @param req 分页请求参数
     * @return {@code Page<T>}
     */
    public static <T> Page<T> toPage(PageReq req) {
        return new Page<>(req.getPage(), req.getPageSize(), req.getSearchCount());
    }

    public static <T> PageVO<T> emptyPage() {
        return new PageVO<>(0L, 0L, 0L, 0L, List.of());
    }

    private static CopyOptions getCopyOption() {
        CopyOptions copyOptions = new CopyOptions();
        copyOptions.ignoreError();
        copyOptions.ignoreNullValue();
        copyOptions.setIgnoreProperties("records");
        return copyOptions;
    }
}
