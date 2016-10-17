package deltix.qsrv.hf.pub;

public enum ConnectorType {
    Data, Trade, MarketMakerData, MarketMakerTrade;

    public boolean isDataConnector() {
        return this == Data || this == MarketMakerData;
    }
}
