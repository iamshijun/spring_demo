package cjava.walker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PatchMakerConfig {

	@Value("${patchmaker.version}")
	private String version;

	public String getVersion() {
		return version;
	}
}
