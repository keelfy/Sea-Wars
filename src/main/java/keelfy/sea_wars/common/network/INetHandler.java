package keelfy.sea_wars.common.network;

/**
 * @author keelfy
 */
public interface INetHandler {

	void onDisconnect(String reason);

	void onConnectionStateTransition(EnumConnectionState stateFrom, EnumConnectionState stateTo);

	void onNetworkTick();

}
