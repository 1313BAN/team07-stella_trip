export type ApiResponse<T> = {
  code: number;
  body: T;
};

export type ApiErrorBody = {
  code: string;
  message: string;
};

export type ApiErrorResponse = ApiResponse<ApiErrorBody>;

export type PagenationResponse<T> = {
  content: T[];
  hasNext: boolean;
  totalPages: number;
  totalElements: number;
  page: number;
  size: number;
  first: boolean;
  last: boolean;
};
