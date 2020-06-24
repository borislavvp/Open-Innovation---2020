module.exports = {
    input: ["./src/**/*.{tsx, ts}"],
    options: {
        debug: true,
        removeUnusedKeys: true,
        func: {
            list: ["t"],
            extensions: [".tsx", ".ts"]
        },
        lngs: ["en"],
        defaultLng: "en",
        defaultValue: (lng, _, key) => {
            if (lng === "en") {
                return key;
            }

            return "__NOT_TRANSLATED__";
        },
        resource: {
            loadPath: "./public/translations/{{lng}}.json",
            savePath: "./public/translations/{{lng}}.json",
            jsonIndent: 4
        },
        nsSeparator: false,
        keySeparator: false
    }
};