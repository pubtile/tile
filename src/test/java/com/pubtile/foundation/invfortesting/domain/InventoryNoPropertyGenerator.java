package com.pubtile.foundation.invfortesting.domain;

import com.pubtile.foundation.domain.instruction.insert.PropertyGenerator;
import com.pubtile.foundation.invfortesting.domain.model.InvForTesting;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 *
 * @author jiayan
 * @version 0.6.17 2021-12-28
 * @since 0.6.17 2021-12-28
 */
@Component("inventoryNoGenerator")
public class InventoryNoPropertyGenerator implements PropertyGenerator<InvForTesting,String> {

    @Override
    public String generate(InvForTesting model) {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.toString();
//        return null;
    }
}
