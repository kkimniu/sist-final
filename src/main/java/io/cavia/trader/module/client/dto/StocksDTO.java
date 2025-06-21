package io.cavia.trader.module.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class StocksDTO {

    private List<StocksOutput> output;
}
