package io.github.yanadroidua.kroute.sample

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ApplicationComponent : KoinComponent {

    val applicationViewModel: ApplicationViewModel by inject<ApplicationViewModel>()
}
