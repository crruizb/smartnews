import { formatDistanceToNow, isValid, parse } from "date-fns";
import { enUS, es } from "date-fns/locale";

const LOCALES = { en: enUS, es } as const;

function localeFor(language: string) {
  return LOCALES[language.slice(0, 2) as keyof typeof LOCALES] ?? enUS;
}

/**
 * Turns the backend's "yyyy-MM-dd HH:mm" timestamp into "2 hours ago".
 * Falls back to the raw string if it cannot be parsed.
 */
export function relativeTime(pubDate: string, language = "en"): string {
  if (!pubDate) return "";
  const parsed = parse(pubDate, "yyyy-MM-dd HH:mm", new Date());
  if (!isValid(parsed)) return pubDate;
  return formatDistanceToNow(parsed, {
    addSuffix: true,
    locale: localeFor(language),
  });
}

/** Absolute, human-friendly date used as a tooltip alongside `relativeTime`. */
export function absoluteTime(pubDate: string, language = "en"): string {
  if (!pubDate) return "";
  const parsed = parse(pubDate, "yyyy-MM-dd HH:mm", new Date());
  if (!isValid(parsed)) return pubDate;
  return parsed.toLocaleString(language.startsWith("es") ? "es-ES" : "en-US", {
    dateStyle: "medium",
    timeStyle: "short",
  });
}

function hostOf(url: string): string | null {
  try {
    return new URL(url).hostname;
  } catch {
    return null;
  }
}

/**
 * Favicon for a news outlet. Prefers its `sourceUrl`, falls back to the
 * article link, and returns `null` when neither yields a host.
 */
export function sourceFavicon(
  sourceUrl: string | undefined,
  link: string | undefined,
  size = 64,
): string | null {
  const host = hostOf(sourceUrl ?? "") ?? hostOf(link ?? "");
  if (!host) return null;
  return `https://www.google.com/s2/favicons?domain=${host}&sz=${size}`;
}

/** One or two letters to show when an outlet has no favicon. */
export function monogram(source: string): string {
  const words = source.trim().split(/\s+/).filter(Boolean);
  if (words.length === 0) return "?";
  if (words.length === 1) return words[0].slice(0, 2).toUpperCase();
  return (words[0][0] + words[1][0]).toUpperCase();
}

/** Initials for the account avatar. */
export function initials(name: string): string {
  const cleaned = name.includes("@") ? name.split("@")[0] : name;
  const parts = cleaned.trim().split(/[\s._-]+/).filter(Boolean);
  if (parts.length === 0) return "?";
  if (parts.length === 1) return parts[0].slice(0, 2).toUpperCase();
  return (parts[0][0] + parts[1][0]).toUpperCase();
}

/** Rough reading time from an excerpt, used on the detail page. */
export function readingTime(text: string): number {
  const words = text.trim().split(/\s+/).filter(Boolean).length;
  return Math.max(1, Math.round(words / 200));
}
