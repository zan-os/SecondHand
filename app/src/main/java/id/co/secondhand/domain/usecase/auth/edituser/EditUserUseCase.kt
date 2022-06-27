package id.co.secondhand.domain.usecase.auth.edituser

import id.co.secondhand.domain.repository.AuthRepository
import javax.inject.Inject


class EditUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
}