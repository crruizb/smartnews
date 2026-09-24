import { useState, useEffect, useRef } from "react";
import { useTranslation } from "react-i18next";
import { Search, X } from "lucide-react";

interface SearchBarProps {
  onSearch: (query: string) => void;
}

export default function SearchBar({ onSearch }: SearchBarProps) {
  const { t } = useTranslation();
  const [inputValue, setInputValue] = useState("");
  const [debouncedValue, setDebouncedValue] = useState("");
  const inputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    const timer = setTimeout(() => {
      setDebouncedValue(inputValue.trim());
    }, 400);
    return () => clearTimeout(timer);
  }, [inputValue]);

  useEffect(() => {
    onSearch(debouncedValue);
  }, [debouncedValue, onSearch]);

  const handleClear = () => {
    setInputValue("");
    setDebouncedValue("");
    onSearch("");
    inputRef.current?.focus();
  };

  return (
    <div className="mx-auto w-full max-w-2xl">
      <div className="relative flex items-center">
        <Search
          className="pointer-events-none absolute left-4 h-5 w-5 text-muted"
          aria-hidden="true"
        />

        <input
          ref={inputRef}
          type="search"
          value={inputValue}
          onChange={(event) => setInputValue(event.target.value)}
          placeholder={t("search.placeholder", "Search news...")}
          aria-label={t("search.placeholder", "Search news...")}
          className="w-full rounded-full border border-line bg-surface py-3 pl-12 pr-12 text-sm text-ink shadow-card transition-colors placeholder:text-muted hover:border-accent/60 focus:border-accent focus:outline-none focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-accent/40 md:text-base [&::-webkit-search-cancel-button]:hidden"
        />

        {inputValue.length > 0 && (
          <button
            type="button"
            onClick={handleClear}
            aria-label={t("search.clear", "Clear search")}
            className="absolute right-3 grid h-8 w-8 place-items-center rounded-full text-muted transition-colors hover:bg-surface-2 hover:text-ink cursor-pointer"
          >
            <X className="h-4.5 w-4.5" aria-hidden="true" />
          </button>
        )}
      </div>
    </div>
  );
}
