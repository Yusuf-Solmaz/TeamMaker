package com.yusuf.domain.use_cases.location

import com.yusuf.domain.repository.LocationRepository
import javax.inject.Inject

class GetLocationNameUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(latitude: Double, longitude: Double) =
        locationRepository.getLocationName(latitude, longitude)
}