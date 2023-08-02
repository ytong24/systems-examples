from transformers import GPT2LMHeadModel, GPT2Tokenizer
import storm

tokenizer = GPT2Tokenizer.from_pretrained("gpt2")
model = GPT2LMHeadModel.from_pretrained("gpt2")

print(tokenizer.decode(outputs[0]))

class SplitSentenceBolt(storm.BasicBolt):
    def process(self, tup):
        testSentence = str(tup.values[0])
        expectedWords = str(tup.values[1])

        inputs = tokenizer.encode(testSentence, return_tensors="pt")
        outputs = model.generate(inputs, max_length=3, do_sample=True)
        createdWords = str(tokenizer.decode(outputs))
        storm.emit([expectedWords, createdWords])

SplitSentenceBolt().run()


