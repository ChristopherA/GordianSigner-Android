package com.bc.gordiansigner.ui.account

import androidx.lifecycle.Lifecycle
import com.bc.gordiansigner.helper.livedata.CompositeLiveData
import com.bc.gordiansigner.helper.livedata.RxLiveDataTransformer
import com.bc.gordiansigner.model.KeyInfo
import com.bc.gordiansigner.service.WalletService
import com.bc.gordiansigner.ui.BaseViewModel
import io.reactivex.Single

class AccountsViewModel(
    lifecycle: Lifecycle,
    private val walletService: WalletService,
    private val rxLiveDataTransformer: RxLiveDataTransformer
) : BaseViewModel(lifecycle) {

    internal val keyInfoLiveData = CompositeLiveData<List<KeyInfo>>()
    internal val deleteKeysLiveData = CompositeLiveData<String>()
    internal val updateKeysLiveData = CompositeLiveData<KeyInfo>()
    internal val hdKeyXprvLiveData = CompositeLiveData<String>()

    fun fetchKeysInfo() {
        keyInfoLiveData.add(
            rxLiveDataTransformer.single(
                walletService.getKeysInfo()
            )
        )
    }

    fun deleteKeyInfo(fingerprintHex: String) {
        deleteKeysLiveData.add(
            rxLiveDataTransformer.single(
                walletService.deleteKeyInfo(fingerprintHex).andThen(Single.just(fingerprintHex))
            )
        )
    }

    fun deleteHDKey(fingerprintHex: String) {
        deleteKeysLiveData.add(
            rxLiveDataTransformer.single(
                walletService.deleteHDKey(fingerprintHex).andThen(Single.just(fingerprintHex))
            )
        )
    }

    fun getHDKeyXprv(fingerprint: String) {
        hdKeyXprvLiveData.add(rxLiveDataTransformer.single(
            walletService.getHDKey(fingerprint).map { it.xprv }
        ))
    }

    fun updateKeyInfo(keyInfo: KeyInfo) {
        updateKeysLiveData.add(
            rxLiveDataTransformer.single(
                walletService.saveKeyInfo(keyInfo)
            )
        )
    }
}