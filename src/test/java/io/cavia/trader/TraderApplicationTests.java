package io.cavia.trader;

import io.cavia.trader.module.game.service.GameAdministrationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TraderApplicationTests {

	@Autowired
	private GameAdministrationService gameAdministrationService;

	@Test
	void contextLoads() {
		gameAdministrationService.createGame();
	}

}
