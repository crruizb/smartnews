import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";

const LANGUAGES = [
    { code: "es", label: "🇪🇸" },
    { code: "en", label: "🇬🇧" },
];

function LanguageSelector() {
    const { i18n } = useTranslation();
    const storedLang = localStorage.getItem("language") || "en";
    const [lang, setLang] = useState(storedLang);

    useEffect(() => {
        i18n.changeLanguage(lang);
        localStorage.setItem("language", lang);
    }, [lang, i18n]);

    const handleChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        setLang(e.target.value);
    };

    return (
        <div className="flex gap-2 items-center">
            <label htmlFor="language-select" className="text-sm">
                🌐
            </label>
            <select
                id="language-select"
                value={lang}
                onChange={handleChange}
                className=" px-2 py-1"
            >
                {LANGUAGES.map((l) => (
                    <option key={l.code} value={l.code}>
                        {l.label}
                    </option>
                ))}
            </select>
        </div>
    );
}

export default LanguageSelector;
