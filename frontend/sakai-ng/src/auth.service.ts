import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { jwtDecode } from 'jwt-decode'; // ← import this

export interface LoginResponse {
    accessToken: string;
    refreshToken: string;
}

export interface JwtPayload {
    sub: string; // username (from your JwtService .subject(username))
    roles?: string[]; // if you put "roles" claim
    iat?: number;
    exp?: number;
    // add other claims if needed
}

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private api = 'http://localhost:8080/api/auth';

    // Signals for current user state
    currentUser = signal<JwtPayload | null>(null);
    // isLoggedIn = computed(() => !!this.currentUser());

    constructor(private http: HttpClient) {
        // Try to restore user from token on app start / refresh
        this.restoreUserFromToken();
    }

    login(username: string, password: string): Observable<LoginResponse> {
        return this.http.post<LoginResponse>(`${this.api}/login`, { username, password }).pipe(
            tap((res) => {
                console.log('Saving token:', res.accessToken ? 'yes' : 'NO TOKEN');
                localStorage.setItem('token', res.accessToken);
                this.updateCurrentUser(res.accessToken);
            })
        );
    }

    logout() {
        localStorage.removeItem('token');
        window.location.href = '/login';
    }

    isLoggedIn = computed(() => {
        const user = this.currentUser();
        console.log('isLoggedIn computed →', !!user); // ← debug
        return !!user;
    });

    private updateCurrentUser(token: string) {
        try {
            const decoded = jwtDecode<JwtPayload>(token);
            console.log('Decoded user:', decoded.sub);
            this.currentUser.set(decoded);
        } catch (e) {
            console.error('Invalid token format', e);
            this.logout(); // clean up bad token
        }
    }

    private restoreUserFromToken() {
        const token = localStorage.getItem('token');
        if (token) {
            this.updateCurrentUser(token);
        }
    }

    // Optional: expose username directly
    username = computed(() => this.currentUser()?.sub ?? 'Guest');
}
