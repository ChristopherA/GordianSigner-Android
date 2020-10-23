package com.bc.gordiansigner.ui.account.add_account

import androidx.lifecycle.Lifecycle
import com.bc.gordiansigner.helper.Error.FINGERPRINT_NOT_MATCH_ERROR
import com.bc.gordiansigner.helper.livedata.CompositeLiveData
import com.bc.gordiansigner.helper.livedata.RxLiveDataTransformer
import com.bc.gordiansigner.model.KeyInfo
import com.bc.gordiansigner.service.WalletService
import com.bc.gordiansigner.ui.BaseViewModel
import io.reactivex.Single

class AddAccountViewModel(
    lifecycle: Lifecycle,
    private val walletService: WalletService,
    private val rxLiveDataTransformer: RxLiveDataTransformer
) : BaseViewModel(lifecycle) {

    internal val importAccountLiveData = CompositeLiveData<String>()

    fun importWallet(phrase: String, alias: String, saveXpv: Boolean, keyInfo: KeyInfo?) {
        importAccountLiveData.add(
            rxLiveDataTransformer.single(
                walletService.importHDKeyWallet(phrase).flatMap { key ->
                    val importedKeyInfo = KeyInfo(key.fingerprintHex, alias, saveXpv)

                    if (keyInfo != null && keyInfo != importedKeyInfo) {
                        Single.error(FINGERPRINT_NOT_MATCH_ERROR)
                    } else {
                        walletService.saveKey(importedKeyInfo, key).map { it.xprv }
                    }
                }
            )
        )
    }
}