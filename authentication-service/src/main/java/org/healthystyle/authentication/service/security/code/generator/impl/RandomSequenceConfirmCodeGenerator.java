package org.healthystyle.authentication.service.security.code.generator.impl;

import java.util.Random;

import org.healthystyle.authentication.service.security.code.generator.ConfirmCodeGenerator;
import org.healthystyle.model.User;
import org.healthystyle.model.security.code.Algorithm;
import org.healthystyle.model.security.code.Type;
import org.springframework.stereotype.Component;

@Component
public class RandomSequenceConfirmCodeGenerator extends ConfirmCodeGenerator {
	private static final int MAX_SIZE = 6;

	public RandomSequenceConfirmCodeGenerator() {
		super();
	}

	public RandomSequenceConfirmCodeGenerator(Algorithm algorithm) {
		super(algorithm);
	}

	@Override
	public String generate(User user) {
		Random randomValue = new Random();
		Random randomCharOrNumber = new Random();

		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < MAX_SIZE; i++) {
			int charOrNumber = randomCharOrNumber.nextInt(((1 - 0) + 1) + 0);
			if (charOrNumber == 0) {
				char c = (char) (randomValue.nextInt(26) + 'a');
				builder.append(Character.toUpperCase(c));
			} else if (charOrNumber == 1) {
				int n = randomValue.nextInt(((9 - 1) + 1) + 1);
				builder.append(n);
			} else {
				throw new RuntimeException("Wrong algorithm. Random value must return 0 or 1, but " + charOrNumber);
			}
		}
		return builder.toString();
	}

}
