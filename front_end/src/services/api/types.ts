export type ApiResponse<T> = {
  code: number;
  body: T;
};

export type ApiErrorBody = {
  code: string;
  message: string;
};

export type ApiErrorResponse = ApiResponse<ApiErrorBody>;

export type User = {
  id: string;
  email: string;
  username: string;
};

export type AuthToken = {
  accessToken: string;
  refreshToken: string;
};
