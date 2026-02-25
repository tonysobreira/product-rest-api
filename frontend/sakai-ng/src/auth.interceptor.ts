import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
    const token = localStorage.getItem('token');

    // Only add header if token exists and is not empty/null/undefined
    if (token && token.trim() !== '' && token !== 'null' && token !== 'undefined') {
        const authReq = req.clone({
            setHeaders: { Authorization: `Bearer ${token}` }
        });
        return next(authReq);
    }

    // Otherwise pass through unchanged (lets login request go without token)
    return next(req);
};
