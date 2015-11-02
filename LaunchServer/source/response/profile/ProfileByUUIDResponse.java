package launchserver.response.profile;

import java.io.IOException;
import java.util.UUID;

import launcher.client.PlayerProfile;
import launcher.serialize.HInput;
import launcher.serialize.HOutput;
import launchserver.LaunchServer;
import launchserver.response.Response;

public final class ProfileByUUIDResponse extends Response {
	public ProfileByUUIDResponse(LaunchServer server, long id, HInput input, HOutput output) {
		super(server, id, input, output);
	}

	@Override
	public void reply() throws IOException {
		UUID uuid = input.readUUID();
		debug("UUID: " + uuid);

		// Verify has such profile
		String username = server.config.authHandler.uuidToUsername(uuid);
		if (username == null) {
			output.writeBoolean(false);
			return;
		}

		// Write profile
		output.writeBoolean(true);
		getProfile(server, uuid, username).write(output);
	}

	public static PlayerProfile getProfile(LaunchServer server, UUID uuid, String username) {
		PlayerProfile.Texture skinURL = server.config.getSkin(username, uuid);
		PlayerProfile.Texture cloakURL = server.config.getCloak(username, uuid);
		return new PlayerProfile(uuid, username, skinURL, cloakURL);
	}
}
