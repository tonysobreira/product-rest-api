import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';

export const authGuard: CanActivateFn = (route, state) => {
    const authService = inject(AuthService);
    const router = inject(Router);

    const isLoggedIn = authService.isLoggedIn();

    console.log('Guard check → isLoggedIn:', isLoggedIn); // ← add this

    if (authService.isLoggedIn()) {
        return true;
    }

    console.log('Guard redirecting to login, attempted:', state.url);

    // Redirect to login and preserve the attempted URL (optional but good UX)
    return router.createUrlTree(['/login'], {
        queryParams: { returnUrl: state.url }
    });
};
