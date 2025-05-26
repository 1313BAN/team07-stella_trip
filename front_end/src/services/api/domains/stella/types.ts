export type StellaParams = {
  planId: number;
  stellaData: string;
};

export type StellaResponseList = {
  stellaList: StellaResponse[];
};

export type StellaResponse = {
  planId: number;
  userId: number;
  stellaData: string;
  stellaLink: string;
  stellaAI: StellaAI;
};

export type StellaAI = {
  cardName: string;
  message: string;
  keywords: string[];
};
