export interface ApiContribution {
  id: number;
  title: string;
  description: string;
  link: string;
  categories: string[];
  pubDate: string;
  creator: string;
  urlImage: string;
  source: string;
  sourceUrl: string;
  vote: number | undefined;
}
