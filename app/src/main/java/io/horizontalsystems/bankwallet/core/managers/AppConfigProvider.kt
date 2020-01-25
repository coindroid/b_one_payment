package io.horizontalsystems.bankwallet.core.managers

import io.horizontalsystems.bankwallet.BuildConfig
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.core.App
import io.horizontalsystems.bankwallet.core.IAppConfigProvider
import io.horizontalsystems.bankwallet.entities.Coin
import io.horizontalsystems.bankwallet.entities.CoinType
import io.horizontalsystems.bankwallet.entities.Currency
import java.math.BigDecimal

class AppConfigProvider : IAppConfigProvider {
    override val companyWebPageLink: String = "https://horizontalsystems.io"
    override val appWebPageLink: String = "https://b-one-payment.money"
    override val reportEmail = "hsdao@protonmail.ch"
    override val reportTelegramGroup = "b-one-payment_wallet"
    override val ipfsId = "QmXTJZBMMRmBbPun6HFt3tmb3tfYF2usLPxFoacL7G5uMX"
    override val ipfsMainGateway = "ipfs-ext.horizontalsystems.xyz"
    override val ipfsFallbackGateway = "ipfs.io"

    override val infuraProjectId = "2a1306f1d12f4c109a4d4fb9be46b02e"
    override val infuraProjectSecret = "fc479a9290b64a84a15fa6544a130218"

    // Bitcoin-Core RPC config
    override val btcCoreRpcUrl: String = "https://damp-old-pond.quiknode.io/38708434-ee69-4c9a-84d7-cb0f7f45f2cc/YiBzRob3cfnxTRSvByiyFh2bU93pKzxeyyTHpacaaPF0YnCg9u_cxvvoPIC-3wh6eaQAPyZh5Hd-fDjLGFXCIA==/"
    override val btcCoreRpcUser: String? = null
    override val btcCoreRpcPassword: String? = null

    override val fiatDecimal: Int = 2
    override val maxDecimal: Int = 8

    override val testMode: Boolean = BuildConfig.testMode

    override val currencies: List<Currency> = listOf(
            Currency(code = "USD", symbol = "\u0024"),
            Currency(code = "EUR", symbol = "\u20AC"),
            Currency(code = "GBP", symbol = "\u00A3"),
            Currency(code = "JPY", symbol = "\u00A5")
    )

    override val localizations: List<String>
        get() {
            val coinsString = App.instance.getString(R.string.localizations)
            return coinsString.split(",")
        }

    override val featuredCoins: List<Coin>
        get() = listOf(
                coins[0],
                coins[1],
                coins[2],
                coins[3],
                coins[4],
                coins[5]
        )

    override val coins: List<Coin> = listOf(
            Coin("BTC",       "Bitcoin",                 "BTC",          8,      CoinType.Bitcoin),
            Coin("ETH",       "Ethereum",                "ETH",         18,      CoinType.Ethereum),
            Coin("BCH",       "Bitcoin Cash",            "BCH",          8,      CoinType.BitcoinCash),
            Coin("DASH",      "Dash",                    "DASH",         8,      CoinType.Dash),
            Coin("BNB",       "Binance DEX",             "BNB",          8,      CoinType.Binance("BNB")),
            Coin("EOS",       "EOS",                     "EOS",          4,      CoinType.Eos("eosio.token", "EOS")),
            Coin("B1P",       "B One Payment",             "B1P",         18,      CoinType.Erc20("0x4b742b5bdb1d252907ae7f399a891d4a178dbc24"))
    )
}
