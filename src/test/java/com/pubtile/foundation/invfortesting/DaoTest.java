package com.pubtile.foundation.invfortesting;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pubtile.foundation.invfortesting.app.InvForTestingApplication;
import com.pubtile.foundation.invfortesting.infrastructure.mybatis.mapper.InvForTestingMapper;
import com.pubtile.foundation.invfortesting.infrastructure.po.InvForTestingPo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * DaoTest
 *
 * @author jiayan
 * @version 0.6.17 2021-12-30
 * @since 0.6.17 2021-12-30
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = InvForTestingApplication.class)
@Slf4j
@Transactional
public class DaoTest {
    @Autowired
    InvForTestingMapper dao;

    @Test
    @DisplayName("测试批量插入")
    public void batchInsert(){
        String vendorCode = "v03";

        List pos =  new ArrayList<>();
        int count = 5;

        for (int i = 0; i < count; i++) {
            int finalI = i;
            InvForTestingPo po = new InvForTestingPo();
            po.setInventoryNo(String.format("INVNO%S", finalI));
            po.setContainerCode("");
            po.setLocationCode(String.format("LX0%S", finalI));
            po.setUniqueCode(String.format("UNIQUE_%S", finalI));
            po.setOwnerCode("owner1");
            po.setSkuId("sku1");
            po.setVendorCode(vendorCode);
            po.setOnHandQty(4);
            po.setAllocatedQty(1);
            po.setSuspenseQty(1);
            po.setInTransitQty(2);
            po.setCreatedBy(3L);
            po.setUpdatedBy(5L);
            po.setEntryOrderType("T12");
            po.setTenantId(1L);
            po.setRevision(1L);
            pos.add(po);
        }

        Integer insertCount = dao.insertBatch(pos);
        assertEquals(count,insertCount,"插入行数");

        List<InvForTestingPo> invForTestingPos =
                dao.selectList(new LambdaQueryWrapper<InvForTestingPo>().eq(InvForTestingPo::getVendorCode, vendorCode));
        assertEquals(count,invForTestingPos.size());

    }
}
