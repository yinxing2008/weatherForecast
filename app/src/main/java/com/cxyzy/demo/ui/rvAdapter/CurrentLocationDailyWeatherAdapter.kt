package com.cxyzy.demo.ui.rvAdapter

import android.text.TextUtils
import com.afollestad.assent.Permission
import com.afollestad.assent.runWithPermissions
import com.cxyzy.demo.R
import com.cxyzy.demo.utils.*
import com.cxyzy.utils.LocationUtils
import com.cxyzy.utils.ext.toast

class CurrentLocationDailyWeatherAdapter(locationId: String) : BaseDailyWeatherAdapter(locationId) {
    private fun locateAndFetchWeatherRequirePermission() {
        activity.runWithPermissions(Permission.ACCESS_FINE_LOCATION) {
            if (LocationUtils.isLocationProviderEnabled(activity)) {
                loadIndicator.showLoading()
                locateAndFetchWeather()
            } else {
                Utils.showAlert(activity.getString(R.string.need_open_location_switch), activity)
            }
        }
    }

    override fun queryWeather() {
        val currentLocationName = SpUtil.getSp(SpConst.CURRENT_LOCATION_NAME)
        if (!TextUtils.isEmpty(currentLocationName)) {
            super@CurrentLocationDailyWeatherAdapter.queryWeather()
            viewModel.updateLocationName(locationId, currentLocationName!!)
        } else {
            locateAndFetchWeatherRequirePermission()
        }
    }

    private fun locateAndFetchWeather() {
        AMapLocationUtil.startLocation(object : Callback {
            override fun onLocateFailure() {
                activity.toast("定位失败")
            }

            override fun onLocateSuccess(location: MyLocation) {
                val locationName = location.cityName.removeSuffix("市")
                SpUtil.saveSp(SpConst.CURRENT_LOCATION_NAME, locationName)
                viewModel.updateLocationName(locationId, locationName)
                super@CurrentLocationDailyWeatherAdapter.queryWeather()
            }
        })
    }

}