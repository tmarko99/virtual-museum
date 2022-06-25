export interface LoginResponse {
    email: string;
    accessToken: string;
    tokenType: string;
    authorities: string[];
}