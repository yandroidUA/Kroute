package io.github.yanadroidua.kroute.sample.authorization.signin

import io.github.yanadroidua.kroute.navigation.arguments.oneTimeArgument

object SignInArgument {
    val Password = oneTimeArgument<String>(key = "sign-in-password")
    val Username = oneTimeArgument<String>(key = "sign-in-username")
}
