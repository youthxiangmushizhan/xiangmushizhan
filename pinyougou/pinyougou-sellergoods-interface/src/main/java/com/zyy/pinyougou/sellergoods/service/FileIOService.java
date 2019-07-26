package com.zyy.pinyougou.sellergoods.service;

import java.util.List;

/**
 * @author: Zyy
 * @date: 2019-07-26 12:55
 * @description:
 * @version:
 */
public interface FileIOService {
    void importFile(List<List<String[]>> list, String className);
}
