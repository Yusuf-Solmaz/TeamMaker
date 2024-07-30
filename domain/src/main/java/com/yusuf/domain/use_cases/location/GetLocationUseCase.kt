package com.yusuf.domain.use_cases.location

import com.yusuf.domain.repository.LocationRepository
import javax.inject.Inject

class GetLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    // invoke function is advantageous because it allows you to call the class as a function
    suspend operator fun invoke() = locationRepository.getLocation()
}